package com.test.spot.strategy;

import com.test.spot.constants.*;
import com.test.spot.data.*;
import com.test.spot.data.event.MatchOrderFillInstruction;
import com.test.spot.data.event.SpotEvent;
import com.test.spot.data.result.MatchOrderCancelResult;
import com.test.spot.data.result.MatchOrderFillResult;
import com.test.spot.data.result.SpotResult;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class SpotMatchFillAction implements ExecuteAction<SpotEvent, SpotResult, SpotStorage> {
    @Override
    public int getEventAction() {
        return EventTypeEnums.MATCH_ORDER_FILL.getEventType();
    }


    @Override
    public SpotResult execute(SpotEvent event, SpotStorage storage) {
        MatchOrderFillInstruction instruction = (MatchOrderFillInstruction) event.getData();

        String symbol = instruction.getSymbol();

        SpotResult result = DataConverter.buildResult(event);

        MatchOrderFillResult matchOrderFillResult = MatchOrderFillResult.build(symbol);

        List<MatchFill> fillList = instruction.getFillList();

        for (MatchFill fill : fillList) {
            try {
                MatchOrderFillResult singleFillResult = executeSingleFill(storage,fill);
                matchOrderFillResult.add(singleFillResult);
            } catch (Exception e) {
                // TODO 这里需要撇异常
                log.error("error execute clearing...");
            }
        }

        result.setData(matchOrderFillResult);
        return result;
    }

    private MatchOrderFillResult executeSingleFill(SpotStorage storage, MatchFill fill) throws Exception {

        String symbol = fill.getSymbol();
        Long userId = fill.getUserId();
        Long orderId = fill.getOrderId();


        MatchOrderFillResult result = new MatchOrderFillResult();

        // 获取用户的信息
        UserStorage userStorage = storage.getUserStage(userId);
        // 获取现货手续费账户
        UserStorage systemFeeUserStorage = storage.getUserStage(SystemUserEnums.SPOT_FEE_USER.getUserId());


        SpotOrder order = userStorage.getOrder(symbol, orderId);

        CurrencyAccount userBaseAccount = userStorage.getCurrencyAccount(order.getBaseCurrency());
        CurrencyAccount userQuoteAccount = userStorage.getCurrencyAccount(order.getQuoteCurrency());

        result.addAccount(userQuoteAccount);
        result.addAccount(userBaseAccount);

        if (order.getSide() == OrderSideEnums.BUY.getSide()) {
            // 买单处理 用户： quote frozen -  base available +

            // 需移除冻结的数量
            BigDecimal removeQuoteFrozenAmount = fill.getPrice().multiply(fill.getQuantity());
            // 需要增加的可用数量
            BigDecimal addBaseAvailableAmount = fill.getQuantity();


            BigDecimal baseAvailable = userBaseAccount.getAvailable().add(addBaseAvailableAmount);
            BigDecimal quoteFrozen = userQuoteAccount.getFrozen().subtract(removeQuoteFrozenAmount);
            if (quoteFrozen.compareTo(BigDecimal.ZERO) < 0) {
                throw new Exception("冻结余额不足");
            }

            // base记账 买入available是增加的
            BigDecimal baseBeforeBalance = userBaseAccount.getAvailable().add(userBaseAccount.getFrozen());
            BigDecimal baseAfterBalance = baseAvailable.add(userBaseAccount.getFrozen());
            // quote记账 买入frozen是减少的
            BigDecimal quoteBeforeBalance = userQuoteAccount.getAvailable().add(userQuoteAccount.getFrozen());
            BigDecimal quoteAfterBalance = userQuoteAccount.getAvailable().add(quoteFrozen);


            // 用户-基础货币 可用+
            userBaseAccount.setAvailable(baseAvailable);
            // 用户-计价货币 冻结-
            userQuoteAccount.setFrozen(quoteFrozen);

            // 更新用户内存账户
            userStorage.updateCurrencyAccount(userBaseAccount);
            userStorage.updateCurrencyAccount(userQuoteAccount);

            // 生成账单 base
            result.addLedger(AccountLedger.builder()
                    .userId(userId)
                    .currency(userBaseAccount.getCurrency())
                    .beforeAmount(baseBeforeBalance)
                    .afterAmount(baseAfterBalance)
                    .amount(addBaseAvailableAmount)
                    .businessId(fill.getId())
                    .businessType(AccountLedgerTypeEnums.ORDER_MATCH_FILL.getLedgerType())
                    .build());
            // 生成账单 quote
            result.addLedger(AccountLedger.builder()
                    .userId(userId)
                    .currency(userQuoteAccount.getCurrency())
                    .beforeAmount(quoteBeforeBalance)
                    .afterAmount(quoteAfterBalance)
                    .amount(removeQuoteFrozenAmount.negate())
                    .businessId(fill.getId())
                    .businessType(AccountLedgerTypeEnums.ORDER_MATCH_FILL.getLedgerType())
                    .build());

        } else {
            // 卖单处理 用户： quote available+  base frozen-
            // 需移除冻结的数量
            BigDecimal removeBaseFrozenAmount = fill.getQuantity();
            // 需要增加的可用数量
            BigDecimal addQuoteAvailableAmount = fill.getQuantity().multiply(fill.getPrice());

            BigDecimal baseFrozen = userBaseAccount.getFrozen().subtract(removeBaseFrozenAmount);
            BigDecimal quoteAvailable = userQuoteAccount.getAvailable().add(addQuoteAvailableAmount);

            if (baseFrozen.compareTo(BigDecimal.ZERO) < 0) {
                throw new Exception("冻结余额不足");
            }

            // base记账 卖出base是减少的
            BigDecimal baseBeforeBalance = userBaseAccount.getAvailable().add(userBaseAccount.getFrozen());
            BigDecimal baseAfterBalance = baseFrozen.add(userBaseAccount.getAvailable());

            // base记账 卖出quote是增加的
            BigDecimal quoteBeforeBalance = userQuoteAccount.getAvailable().add(userBaseAccount.getFrozen());
            BigDecimal quoteAfterBalance = userQuoteAccount.getFrozen().add(quoteAvailable);

            // 用户-基础货币 可用+
            userBaseAccount.setFrozen(baseFrozen);
            // 用户-计价货币 冻结-
            userQuoteAccount.setAvailable(quoteAvailable);

            // 更新用户内存账户
            userStorage.updateCurrencyAccount(userBaseAccount);
            userStorage.updateCurrencyAccount(userQuoteAccount);



            // 生成账单 base
            result.addLedger(AccountLedger.builder()
                    .userId(userId)
                    .currency(userBaseAccount.getCurrency())
                    .beforeAmount(baseBeforeBalance)
                    .afterAmount(baseAfterBalance)
                    .amount(removeBaseFrozenAmount.negate())
                    .businessId(fill.getId())
                    .businessType(AccountLedgerTypeEnums.ORDER_MATCH_FILL.getLedgerType())
                    .build());
            // 生成账单 quote
            result.addLedger(AccountLedger.builder()
                    .userId(userId)
                    .currency(userQuoteAccount.getCurrency())
                    .beforeAmount(quoteBeforeBalance)
                    .afterAmount(quoteAfterBalance)
                    .amount(addQuoteAvailableAmount)
                    .businessId(fill.getId())
                    .businessType(AccountLedgerTypeEnums.ORDER_MATCH_FILL.getLedgerType())
                    .build());
        }

        // 手续费
        FeeCompute feeCompute = this.computeFee(order, fill);
        BigDecimal fee = feeCompute.getFee();
        String feeCurrency = feeCompute.getFeeCurrency();

        CurrencyAccount userFeeAccount = userStorage.getCurrencyAccount(feeCurrency);
        // 如果抵扣账户不足的话， 则就按照正常手续费来收， 如果够，就用抵扣账户来扣钱
        if (!feeCompute.getNormal() && userFeeAccount.getAvailable().compareTo(fee) < 0) {
            fee = feeCompute.getNormalFee();
            feeCurrency = feeCompute.getNormalFeeCurrency();
            userFeeAccount = userStorage.getCurrencyAccount(feeCompute.getNormalFeeCurrency());
        } else {
            result.addAccount(userFeeAccount);
        }


        // 用户手续费账户-可用-
        BigDecimal userFeeAvailable = userFeeAccount.getAvailable().subtract(fee);

        BigDecimal userFeeBeforeBalance = userFeeAccount.getAvailable().add(userFeeAccount.getFrozen());
        BigDecimal userFeeAfterBalance = userFeeAvailable.add(userFeeAccount.getFrozen());

        userFeeAccount.setAvailable(userFeeAvailable);
        // 更新用户手续费
        userStorage.updateCurrencyAccount(userFeeAccount);

        // 用户手续费账单
        result.addLedger(AccountLedger.builder()
                .userId(userId)
                .currency(feeCurrency)
                .beforeAmount(userFeeBeforeBalance)
                .afterAmount(userFeeAfterBalance)
                .amount(fee.negate())
                .businessId(fill.getId())
                .businessType(AccountLedgerTypeEnums.ORDER_MATCH_FILL_FEE.getLedgerType())
                .build());


        // 系统手续费账户-可用+
        CurrencyAccount systemFeeCurrencyAccount = systemFeeUserStorage.getCurrencyAccount(feeCurrency);
        BigDecimal feeAvailable = systemFeeCurrencyAccount.getAvailable().add(fee);

        BigDecimal systemFeeBeforeBalance = systemFeeCurrencyAccount.getAvailable().add(systemFeeCurrencyAccount.getFrozen());
        BigDecimal systemFeeAfterBalance = feeAvailable.add(systemFeeCurrencyAccount.getFrozen());

        systemFeeCurrencyAccount.setAvailable(feeAvailable);
        // 更新系统手续费
        systemFeeUserStorage.updateCurrencyAccount(systemFeeCurrencyAccount);
        result.addAccount(systemFeeCurrencyAccount);

        // 系统手续费账单
        result.addLedger(AccountLedger.builder()
                .userId(systemFeeUserStorage.getUserId())
                .currency(feeCurrency)
                .beforeAmount(systemFeeBeforeBalance)
                .afterAmount(systemFeeAfterBalance)
                .amount(fee)
                .businessId(fill.getId())
                .businessType(AccountLedgerTypeEnums.ORDER_MATCH_FILL_FEE.getLedgerType())
                .build());

        // 更新订单
        BigDecimal finishedQuantity = order.getFinishQuantity().add(fill.getQuantity());
        OrderStateEnums newOrderState = OrderStateEnums.find(fill.getOrderState());
        order.setFinishQuantity(finishedQuantity);
        order.setState(newOrderState.getState());

        if (newOrderState.getFinished() == 1) {
            userStorage.removeOrder(order);
        }

        // 更新内存
        storage.updateUserStage(userStorage);
        storage.updateUserStage(systemFeeUserStorage);

        result.addOrder(order);
        result.addFill(fill);

        return result;
    }

    private FeeCompute computeFee(SpotOrder order, MatchFill fill) {

        String feeCurrency = order.getFeeCurrency();
        String normalCurrency;

        BigDecimal needPayFeeQuantity;
        if (order.getSide() == OrderSideEnums.BUY.getSide()) {
            // 如果是买单，成交后得到的是base， 所以应缴手续费的金额就是amount
            needPayFeeQuantity = fill.getQuantity();
            normalCurrency = order.getBaseCurrency();
        } else {
            // 如果是卖单，成交后得到的是quote， 所以应缴手续费的金额就是amount*price
            needPayFeeQuantity = fill.getQuantity().multiply(fill.getPrice());
            normalCurrency = order.getQuoteCurrency();
        }

        // 手续费 = 应缴手续费的金额 * 手续费率， 需要区分taker,maker
        BigDecimal normalFee;
        if (fill.getRole() == MatchRoleEnums.TAKER.getRole()) {
            normalFee = needPayFeeQuantity.multiply(order.getTakerFeeRate());
        } else {
            normalFee = needPayFeeQuantity.multiply(order.getMakerFeeRate());
        }

        Boolean isNormal = true;
        BigDecimal fee = normalFee;
        // 如果交手续费的币种即不是计价货币，也不是基础货币， 说明是用其他币种进行抵扣了
        if (!feeCurrency.equalsIgnoreCase(order.getBaseCurrency()) && !feeCurrency.equalsIgnoreCase(order.getQuoteCurrency())) {
            // TODO 手续费抵扣计算
            isNormal = false;
        }

        return FeeCompute.builder()
                .feeCurrency(feeCurrency)
                .fee(fee)
                .normal(isNormal)
                .normalFeeCurrency(normalCurrency)
                .normalFee(normalFee)
                .build();
    }


    private SpotResult buildErrorResult(SpotResult result, MatchOrderCancelResult matchOrderCancelResult, SpotOrder order, ResultErrorEnums errorEnums) {
        matchOrderCancelResult.setOrder(order);
        matchOrderCancelResult.setResult(errorEnums.getCode());
        matchOrderCancelResult.setMessage(errorEnums.getMessage());
        result.setData(matchOrderCancelResult);
        return null;
    }
}
