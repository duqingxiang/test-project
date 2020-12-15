package com.test.spot;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.test.spot.constants.*;
import com.test.spot.data.DataConverter;
import com.test.spot.data.MatchFill;
import com.test.spot.data.SpotStorage;
import com.test.spot.data.event.*;
import com.test.spot.data.result.SpotResult;
import com.test.spot.strategy.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class SpotLogicUnit {

    private SpotStorage storage = new SpotStorage();

    private SpotExecuteStrategy<SpotEvent, SpotResult, SpotStorage> strategy;

    public SpotLogicUnit() {
        strategy = new SpotExecuteStrategy<>();
        strategy.registerAction(new SpotAccountTransferInAction())
                .registerAction(new SpotAccountTransferOutAction())
                .registerAction(new SpotCreateOrderAction())
                .registerAction(new SpotCancelOrderAction())
                .registerAction(new SpotMatchCancelAction())
                .registerAction(new SpotMatchFillAction())
        ;

    }


    public SpotResult execute(SpotEvent event) {
        SpotResult result;
        try {
            result = strategy.dispatch(event.getEventType(), event, storage);
        } catch (Exception e) {

            result = DataConverter.buildResult(event);
            result.setResultType(ResultTypeEnums.EXCEPTION.getResultType());
            result.setMessage(e.getMessage());
            log.error(" execute event exception: {}", JSON.toJSONString(event), e);
        }
        return result;
    }


    public static void main(String[] args) {

        AtomicLong incr = new AtomicLong();
        String symbol = "btcusdt";
        String baseCurrency = "btc";
        String quoteCurrency = "usdt";
        Long userId = 100001L;

        SpotLogicUnit logicUnit = new SpotLogicUnit();

        // 转入
        SpotResult accountTransferInResult = logicUnit.execute(SpotEvent.builder()
                .eventType(EventTypeEnums.ACCOUNT_TRANSFER_IN.getEventType())
                .sequence(incr.addAndGet(1))
                .userId(userId)
                .data(AccountTransferInstruction.builder()
                        .id(incr.addAndGet(1))
                        .userId(userId)
                        .amount(new BigDecimal(11000))
                        .currency(quoteCurrency)
                        .build())
                .build());

        System.out.println("===>" + JSON.toJSONString(accountTransferInResult));
        System.out.println("==++==>" + JSON.toJSONString(logicUnit.storage));

        // 转出
        SpotResult accountTransferOutResult = logicUnit.execute(SpotEvent.builder()
                .eventType(EventTypeEnums.ACCOUNT_TRANSFER_OUT.getEventType())
                .sequence(incr.addAndGet(1))
                .userId(userId)
                .data(AccountTransferInstruction.builder()
                        .id(incr.addAndGet(1))
                        .userId(userId)
                        .amount(new BigDecimal(1000))
                        .currency(quoteCurrency)
                        .build())
                .build());


        System.out.println("===>" + JSON.toJSONString(accountTransferOutResult));
        System.out.println("==++==>" + JSON.toJSONString(logicUnit.storage));

        // 转入
        SpotResult accountTransferInResult1 = logicUnit.execute(SpotEvent.builder()
                .eventType(EventTypeEnums.ACCOUNT_TRANSFER_IN.getEventType())
                .sequence(incr.addAndGet(1))
                .userId(userId)
                .data(AccountTransferInstruction.builder()
                        .id(incr.addAndGet(1))
                        .userId(userId)
                        .amount(new BigDecimal(100))
                        .currency(baseCurrency)
                        .build())
                .build());

        System.out.println("===>" + JSON.toJSONString(accountTransferInResult1));
        System.out.println("==++==>" + JSON.toJSONString(logicUnit.storage));

        // 下单
        Long buyId = 12345678800001L;
        SpotResult createOrderResultBuy = logicUnit.execute(SpotEvent.builder()
                .eventType(EventTypeEnums.ORDER_CREATE.getEventType())
                .sequence(incr.addAndGet(1))
                .userId(userId)
                .data(CreateOrderInstruction.builder()
                        .id(buyId)
                        .userId(userId)
                        .symbol(symbol)
                        .side(OrderSideEnums.BUY.getSide())
                        .type(OrderTypeEnums.LIMIT.getType())
                        .price(new BigDecimal("1000"))
                        .quantity(new BigDecimal("1"))
                        .amount(new BigDecimal("1000"))
                        .state(OrderStateEnums.PREPARE.getState())
                        .baseCurrency(baseCurrency)
                        .quoteCurrency(quoteCurrency)
                        .feeCurrency(baseCurrency)
                        .takerFeeRate(new BigDecimal("0.002"))
                        .makerFeeRate(new BigDecimal("0.001"))
                        .build())
                .build());

        System.out.println("===>" + JSON.toJSONString(createOrderResultBuy));
        System.out.println("==++==>" + JSON.toJSONString(logicUnit.storage));

        // 下单
        Long sellId = 12345678800002L;
        SpotResult createOrderResultSell = logicUnit.execute(SpotEvent.builder()
                .eventType(EventTypeEnums.ORDER_CREATE.getEventType())
                .sequence(incr.addAndGet(1))
                .userId(userId)
                .data(CreateOrderInstruction.builder()
                        .id(sellId)
                        .userId(userId)
                        .symbol(symbol)
                        .side(OrderSideEnums.SELL.getSide())
                        .type(OrderTypeEnums.LIMIT.getType())
                        .price(new BigDecimal("1000"))
                        .quantity(new BigDecimal("1"))
                        .amount(new BigDecimal("1000"))
                        .state(OrderStateEnums.PREPARE.getState())
                        .baseCurrency(baseCurrency)
                        .quoteCurrency(quoteCurrency)
                        .feeCurrency(quoteCurrency)
                        .takerFeeRate(new BigDecimal("0.002"))
                        .makerFeeRate(new BigDecimal("0.001"))
                        .build())
                .build());

        System.out.println("===>" + JSON.toJSONString(createOrderResultSell));
        System.out.println("==++==>" + JSON.toJSONString(logicUnit.storage));

        // 下单（为了撤单）
        Long cancelId = 12345678800003L;
        SpotResult createOrderResultCancel = logicUnit.execute(SpotEvent.builder()
                .eventType(EventTypeEnums.ORDER_CREATE.getEventType())
                .sequence(incr.addAndGet(1))
                .userId(userId)
                .data(CreateOrderInstruction.builder()
                        .id(cancelId)
                        .userId(userId)
                        .symbol(symbol)
                        .side(OrderSideEnums.SELL.getSide())
                        .type(OrderTypeEnums.LIMIT.getType())
                        .price(new BigDecimal("1000"))
                        .quantity(new BigDecimal("1"))
                        .amount(new BigDecimal("1000"))
                        .state(OrderStateEnums.PREPARE.getState())
                        .baseCurrency(baseCurrency)
                        .quoteCurrency(quoteCurrency)
                        .feeCurrency(quoteCurrency)
                        .takerFeeRate(new BigDecimal("0.002"))
                        .makerFeeRate(new BigDecimal("0.001"))
                        .build())
                .build());

        System.out.println("===>" + JSON.toJSONString(createOrderResultCancel));
        System.out.println("==++==>" + JSON.toJSONString(logicUnit.storage));

        //  撤单
        SpotResult cancelResult = logicUnit.execute(SpotEvent.builder()
                .eventType(EventTypeEnums.ORDER_CANCEL.getEventType())
                .sequence(incr.addAndGet(1))
                .userId(userId)
                .data(CancelOrderInstruction.builder()
                        .symbol(symbol)
                        .id(cancelId)
                        .userId(userId)
                        .build())
                .build());

        System.out.println("===>" + JSON.toJSONString(cancelResult));
        System.out.println("==++==>" + JSON.toJSONString(logicUnit.storage));

        //  撮合-撤单
        SpotResult matchCancelResult = logicUnit.execute(SpotEvent.builder()
                .eventType(EventTypeEnums.MATCH_ORDER_CANCEL.getEventType())
                .sequence(incr.addAndGet(1))
                .userId(userId)
                .data(MatchOrderCancelInstruction.builder()
                        .symbol(symbol)
                        .orderId(cancelId)
                        .userId(userId)
                        .build())
                .build());

        System.out.println("===>" + JSON.toJSONString(matchCancelResult));
        System.out.println("==++==>" + JSON.toJSONString(logicUnit.storage));


        // 撮合
        SpotResult matchFillResult = logicUnit.execute(SpotEvent.builder()
                .eventType(EventTypeEnums.MATCH_ORDER_FILL.getEventType())
                .sequence(incr.addAndGet(1))
                .userId(userId)
                .data(MatchOrderFillInstruction.builder()
                        .symbol(symbol)
                        .fillList(Lists.newArrayList(MatchFill.builder()
                                        .id(incr.addAndGet(1))
                                        .symbol(symbol)
                                        .userId(userId)
                                        .orderId(buyId)
                                        .price(new BigDecimal("1000"))
                                        .quantity(new BigDecimal("1"))
                                        .role(MatchRoleEnums.MAKER.getRole())
                                        .orderState(OrderStateEnums.FILLED.getState())
                                        .build(),
                                MatchFill.builder()
                                        .id(incr.addAndGet(1))
                                        .symbol(symbol)
                                        .userId(userId)
                                        .orderId(sellId)
                                        .price(new BigDecimal("1000"))
                                        .quantity(new BigDecimal("1"))
                                        .role(MatchRoleEnums.TAKER.getRole())
                                        .orderState(OrderStateEnums.FILLED.getState())
                                        .build()))
                        .build())
                .build());

        System.out.println("===>" + JSON.toJSONString(matchFillResult));
        System.out.println("==++==>" + JSON.toJSONString(logicUnit.storage));

    }

}
