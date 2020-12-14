package com.test.spot.strategy;

import com.alibaba.fastjson.JSON;
import com.test.spot.constants.*;
import com.test.spot.data.*;
import com.test.spot.data.event.CreateOrderInstruction;
import com.test.spot.data.event.SpotEvent;
import com.test.spot.data.result.CreateOrderResult;
import com.test.spot.data.result.SpotResult;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public class SpotCreateOrderAction implements ExecuteAction<SpotEvent, SpotResult, SpotStorage> {

    @Override
    public int getEventAction() {
        return EventTypeEnums.ORDER_CREATE.getEventType();
    }

    @Override
    public SpotResult execute(SpotEvent event, SpotStorage storage) {
        CreateOrderInstruction instruction = (CreateOrderInstruction) event.getData();

        Long orderId = instruction.getId();
        String symbol = instruction.getSymbol();
        Long userId = instruction.getUserId();
        SpotOrder order = SpotOrder.convert(instruction);
        OrderSideEnums orderSide = OrderSideEnums.find(instruction.getSide());
        OrderTypeEnums orderType = OrderTypeEnums.find(instruction.getType());

        SpotResult result = DataConverter.buildResult(event);
        CreateOrderResult createOrderResult = CreateOrderResult.builder()
                .id(orderId)
                .userId(userId)
                .symbol(symbol)
                .result(ResultErrorEnums.SUCCESS.getCode())
                .order(order)
                .build();

        // 取出用户的币种账户
        UserStorage userStorage = storage.getUserStage(userId);

        // 在整个engine里查询一下去重信息
        boolean cacheExist = storage.getTransactionCache().checkAndPutTransactionId(event.getEventType(), symbol, orderId);
        if (cacheExist) {
            log.error(" execute engine duplicate order :{}", JSON.toJSONString(event));
            order.setState(OrderStateEnums.REJECTED.getState());
            return buildErrorResult(result, createOrderResult, order, ResultErrorEnums.ENGINE_TRANSACTION_DUPLICATE);
        }

        // 在个人信息里检查一下订单，进行去重
        SpotOrder existOrder = userStorage.getOrder(symbol, orderId);
        if (Objects.nonNull(existOrder)) {
            log.error(" execute user duplicate order :{}", JSON.toJSONString(event));
            order.setState(OrderStateEnums.REJECTED.getState());
            return buildErrorResult(result, createOrderResult, order, ResultErrorEnums.ORDER_DUPLICATE);
        }

        String changeCurrency;
        BigDecimal changeAmount;

        // 买单
        if (OrderSideEnums.BUY == orderSide) {
            // 买单都是用计价货币来购买的
            changeCurrency = instruction.getQuoteCurrency();
            if (OrderTypeEnums.LIMIT == orderType) {
                // 现价买单需要冻结的金额 = price * quantity
                changeAmount = instruction.getPrice().multiply(instruction.getQuantity());
            } else if (OrderTypeEnums.MARKET == orderType) {
                // 市价买单需要冻结的金额 = amount
                changeAmount = instruction.getAmount();
            } else {
                log.error(" buy limit unsupported order type: {}", JSON.toJSONString(event));
                order.setState(OrderStateEnums.REJECTED.getState());
                return buildErrorResult(result, createOrderResult, order, ResultErrorEnums.UNSUPPORTED_ORDER_TYPE);
            }
        } else if (OrderSideEnums.SELL == orderSide) {
            // 卖单都是以计价货币计算，并且都是按照数量进行冻结的
            changeCurrency = instruction.getBaseCurrency();
            changeAmount = instruction.getQuantity();
        } else {
            log.error(" unsupported order side: {}", JSON.toJSONString(event));
            order.setState(OrderStateEnums.REJECTED.getState());
            return buildErrorResult(result, createOrderResult, order, ResultErrorEnums.UNSUPPORTED_ORDER_SIDE);
        }

        // 获取币种账户，并对可用余额进行比较检查
        CurrencyAccount currencyAccount = userStorage.getCurrencyAccount(changeCurrency);
        BigDecimal available = currencyAccount.getAvailable();
        if (currencyAccount.getAvailable().compareTo(changeAmount) < 0) {
            log.error(" create order available not enough : current balance:{}   input:{} ", JSON.toJSONString(currencyAccount), JSON.toJSONString(event));
            order.setState(OrderStateEnums.REJECTED.getState());
            return buildErrorResult(result, createOrderResult, order, ResultErrorEnums.ACCOUNT_AVAILABLE_NOT_ENOUGH, available.toPlainString());
        }

        BigDecimal frozen = currencyAccount.getFrozen();

        // 冻结可用金额， available = available - change ， frozen = frozen + change
        currencyAccount.setAvailable(available.subtract(changeAmount));
        currencyAccount.setFrozen(frozen.add(changeAmount));


        //修改订单状态
        order.setState(OrderStateEnums.CREATE.getState());

        // 更新账户
        userStorage.updateCurrencyAccount(currencyAccount);
        // 更新订单
        userStorage.updateOrder(order);
        // 更新内存
        storage.updateUserStage(userStorage);

        // 正常返回
        createOrderResult.setOrder(order);
        createOrderResult.setCurrencyAccount(currencyAccount);
        result.setData(createOrderResult);
        return result;
    }


    private SpotResult buildErrorResult(SpotResult result, CreateOrderResult createOrderResult, SpotOrder order, ResultErrorEnums errorEnums) {
        createOrderResult.setOrder(order);
        createOrderResult.setResult(errorEnums.getCode());
        createOrderResult.setMessage(errorEnums.getMessage());
        result.setData(createOrderResult);
        return null;
    }

    private SpotResult buildErrorResult(SpotResult result, CreateOrderResult createOrderResult, SpotOrder order, ResultErrorEnums errorEnums, String... args) {
        createOrderResult.setOrder(order);
        createOrderResult.setResult(errorEnums.getCode());
        createOrderResult.setMessage(errorEnums.getMessage(args));
        result.setData(createOrderResult);
        return null;
    }
}
