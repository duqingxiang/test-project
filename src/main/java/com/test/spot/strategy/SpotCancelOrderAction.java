package com.test.spot.strategy;

import com.alibaba.fastjson.JSON;
import com.test.spot.constants.*;
import com.test.spot.data.*;
import com.test.spot.data.event.CancelOrderInstruction;
import com.test.spot.data.event.CreateOrderInstruction;
import com.test.spot.data.event.SpotEvent;
import com.test.spot.data.result.CancelOrderResult;
import com.test.spot.data.result.CreateOrderResult;
import com.test.spot.data.result.SpotResult;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public class SpotCancelOrderAction implements ExecuteAction<SpotEvent, SpotResult, SpotStorage> {

    @Override
    public int getEventAction() {
        return EventTypeEnums.ORDER_CANCEL.getEventType();
    }

    @Override
    public SpotResult execute(SpotEvent event, SpotStorage storage) {
        CancelOrderInstruction instruction = (CancelOrderInstruction) event.getData();

        Long orderId = instruction.getId();
        String symbol = instruction.getSymbol();
        Long userId = instruction.getUserId();

        SpotResult result = DataConverter.buildResult(event);
        CancelOrderResult cancelOrderResult = CancelOrderResult.builder()
                .id(orderId)
                .symbol(symbol)
                .userId(userId)
                .result(ResultErrorEnums.SUCCESS.getCode())
                .build();

        // 取出用户的币种账户
        UserStorage userStorage = storage.getUserStage(userId);

        // 在个人信息里检查一下订单，进行去重
        SpotOrder existOrder = userStorage.getOrder(symbol, orderId);
        if (Objects.isNull(existOrder)) {
            log.error(" cancel order not exist :{}", JSON.toJSONString(event));
            return buildErrorResult(result, cancelOrderResult, ResultErrorEnums.ORDER_NOT_EXIST);
        }

        OrderStateEnums existOrderState = OrderStateEnums.find(existOrder.getState());
        // 已完成状态的订单不能继续撤销，并且理论上应该不在内存中存在
        if (existOrderState.getFinished() == 1) {
            // 移除订单
            userStorage.removeOrder(existOrder);
            // 更新内存
            storage.updateUserStage(userStorage);
            return buildErrorResult(result, cancelOrderResult, ResultErrorEnums.ORDER_NOT_EXIST);
        }


        //撤单时，仅将订单状态修改为 canceling即可
        existOrder.setState(OrderStateEnums.CANCELING.getState());

        // 更新订单
        userStorage.updateOrder(existOrder);
        // 更新内存
        storage.updateUserStage(userStorage);

        // 正常返回
        cancelOrderResult.setOrder(existOrder);
        result.setData(cancelOrderResult);
        return result;
    }


    private SpotResult buildErrorResult(SpotResult result, CancelOrderResult createOrderResult,  ResultErrorEnums errorEnums) {
        createOrderResult.setResult(errorEnums.getCode());
        createOrderResult.setMessage(errorEnums.getMessage());
        result.setData(createOrderResult);
        return result;
    }

}
