package com.test.spot.strategy;

import com.test.spot.constants.EventTypeEnums;
import com.test.spot.constants.OrderSideEnums;
import com.test.spot.constants.OrderStateEnums;
import com.test.spot.constants.ResultErrorEnums;
import com.test.spot.data.*;
import com.test.spot.data.event.MatchOrderCancelInstruction;
import com.test.spot.data.event.SpotEvent;
import com.test.spot.data.result.CreateOrderResult;
import com.test.spot.data.result.MatchOrderCancelResult;
import com.test.spot.data.result.SpotResult;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public class SpotMatchCancelAction implements ExecuteAction<SpotEvent, SpotResult, SpotStorage> {
    @Override
    public int getEventAction() {
        return EventTypeEnums.MATCH_ORDER_CANCEL.getEventType();
    }


    @Override
    public SpotResult execute(SpotEvent event, SpotStorage storage) {
        MatchOrderCancelInstruction instruction = (MatchOrderCancelInstruction) event.getData();

        Long orderId = instruction.getOrderId();
        Long userId = instruction.getUserId();
        String symbol = instruction.getSymbol();

        SpotResult result = DataConverter.buildResult(event);

        MatchOrderCancelResult matchOrderCancelResult = MatchOrderCancelResult.builder()
                .userId(userId)
                .symbol(symbol)
                .orderId(orderId)
                .build();


        UserStorage userStorage = storage.getUserStage(userId);

        SpotOrder order = userStorage.getOrder(symbol, orderId);
        if (Objects.isNull(order)) {
            return buildErrorResult(result, matchOrderCancelResult, null, ResultErrorEnums.ORDER_NOT_EXIST);
        }

        // 订单撤销应该早就已经在内存中修改为canceling状态了
        if (order.getState() != OrderStateEnums.CANCELING.getState()) {
            return buildErrorResult(result, matchOrderCancelResult, null, ResultErrorEnums.ORDER_STATE_ERROR);
        }

        String unfrozenCurrency;
        BigDecimal unfrozenAmount;
        BigDecimal lastQuantity = order.getQuantity().subtract(order.getFinishQuantity());
        if (order.getSide() == OrderSideEnums.BUY.getSide()) {
            unfrozenCurrency = order.getQuoteCurrency();
            unfrozenAmount = order.getPrice().multiply(lastQuantity);
        } else {
            unfrozenCurrency = order.getBaseCurrency();
            unfrozenAmount = lastQuantity;
        }

        Integer newOrderState = OrderStateEnums.CANCELED.getState();
        if (order.getFinishQuantity().compareTo(BigDecimal.ZERO) > 0) {
            newOrderState = OrderStateEnums.PARTIAL_CANCELED.getState();
        }

        // 修改订单状态
        order.setState(newOrderState);


        // 更新资产 frozen- available+
        CurrencyAccount currencyAccount = userStorage.getCurrencyAccount(unfrozenCurrency);
        BigDecimal frozen = currencyAccount.getFrozen().subtract(unfrozenAmount);
        BigDecimal available = currencyAccount.getAvailable().add(unfrozenAmount);

        currencyAccount.setFrozen(frozen);
        currencyAccount.setAvailable(available);

        // 更新账户内存
        userStorage.updateCurrencyAccount(currencyAccount);

        // 将订单移除内存
        userStorage.removeOrder(order);

        // 更新内存
        storage.updateUserStage(userStorage);

        // 构建结果
        matchOrderCancelResult.setOrder(order);
        matchOrderCancelResult.setAccount(currencyAccount);

        result.setData(matchOrderCancelResult);
        return result;
    }


    private SpotResult buildErrorResult(SpotResult result, MatchOrderCancelResult matchOrderCancelResult, SpotOrder order, ResultErrorEnums errorEnums) {
        matchOrderCancelResult.setOrder(order);
        matchOrderCancelResult.setResult(errorEnums.getCode());
        matchOrderCancelResult.setMessage(errorEnums.getMessage());
        result.setData(matchOrderCancelResult);
        return null;
    }
}
