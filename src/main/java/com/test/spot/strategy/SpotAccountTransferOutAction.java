package com.test.spot.strategy;

import com.test.spot.constants.AccountLedgerTypeEnums;
import com.test.spot.constants.EventTypeEnums;
import com.test.spot.constants.ResultErrorEnums;
import com.test.spot.data.*;
import com.test.spot.data.event.AccountTransferInstruction;
import com.test.spot.data.event.SpotEvent;
import com.test.spot.data.result.AccountTransferResult;
import com.test.spot.data.result.SpotResult;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class SpotAccountTransferOutAction implements ExecuteAction<SpotEvent, SpotResult, SpotStorage> {

    @Override
    public int getEventAction() {
        return EventTypeEnums.ACCOUNT_TRANSFER_OUT.getEventType();
    }

    @Override
    public SpotResult execute(SpotEvent event, SpotStorage storage) {
        AccountTransferInstruction instruction = (AccountTransferInstruction) event.getData();
        SpotResult result = DataConverter.buildResult(event);

        Long businessId = instruction.getId();
        Long userId = instruction.getUserId();
        String currency = instruction.getCurrency();
        BigDecimal changeAmount = instruction.getAmount();
        AccountTransferResult transferResult = AccountTransferResult.builder()
                .id(businessId)
                .userId(userId)
                .currency(currency)
                .amount(changeAmount)
                .result(ResultErrorEnums.SUCCESS.getCode())
                .build();

        // 取出用户的币种账户
        UserStorage userStorage = storage.getUserStage(userId);
        CurrencyAccount currencyAccount = userStorage.getCurrencyAccount(currency);
        BigDecimal available = currencyAccount.getAvailable();

        // 如果用户的余额小于用户转出金额，则返回异常
        if (available.compareTo(changeAmount) < 0) {
            transferResult.setResult(ResultErrorEnums.ACCOUNT_AVAILABLE_NOT_ENOUGH.getCode());
            transferResult.setMessage(ResultErrorEnums.ACCOUNT_AVAILABLE_NOT_ENOUGH.getMessage(available.toPlainString()));
            result.setData(transferResult);
            return result;
        }

        // 计算账单上变化前后的值
        BigDecimal beforeBalance = available.add(currencyAccount.getFrozen());
        BigDecimal afterBalance = beforeBalance.subtract(changeAmount);
        // 计算转出后的可用余额
        BigDecimal afterAvailable = available.subtract(changeAmount);

        // 更新可用余额
        currencyAccount.setAvailable(afterAvailable);
        userStorage.updateCurrencyAccount(currencyAccount);
        storage.updateUserStage(userStorage);

        // 生成账单
        AccountLedger ledger = AccountLedger.builder()
                .userId(instruction.getUserId())
                .currency(currency)
                .beforeAmount(beforeBalance)
                .afterAmount(afterBalance)
                .amount(changeAmount.negate())
                .businessId(businessId)
                .businessType(AccountLedgerTypeEnums.ACCOUNT_TRANSFER_IN.getLedgerType())
                .build();


        // 生成输出结果
        transferResult.setCurrencyAccount(currencyAccount);
        transferResult.setLedger(ledger);
        result.setData(transferResult);
        return result;
    }


}
