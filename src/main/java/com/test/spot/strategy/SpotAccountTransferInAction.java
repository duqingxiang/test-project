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
public class SpotAccountTransferInAction implements ExecuteAction<SpotEvent, SpotResult, SpotStorage> {

    @Override
    public int getEventAction() {
        return EventTypeEnums.ACCOUNT_TRANSFER_IN.getEventType();
    }

    @Override
    public SpotResult execute(SpotEvent event, SpotStorage storage) {
        AccountTransferInstruction instruction = (AccountTransferInstruction) event.getData();

        Long businessId = instruction.getId();
        Long userId = instruction.getUserId();
        String currency = instruction.getCurrency();
        BigDecimal changeAmount = instruction.getAmount();

        // 取出用户的币种账户
        UserStorage userStorage = storage.getUserStage(userId);
        CurrencyAccount currencyAccount = userStorage.getCurrencyAccount(currency);

        // 计算转入后的可用余额
        BigDecimal beforeBalance = currencyAccount.getAvailable().add(currencyAccount.getFrozen());
        BigDecimal afterBalance = beforeBalance.add(changeAmount);
        BigDecimal afterAvailable = currencyAccount.getAvailable().add(changeAmount);

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
                .amount(changeAmount)
                .businessId(businessId)
                .businessType(AccountLedgerTypeEnums.ACCOUNT_TRANSFER_IN.getLedgerType())
                .build();


        // 生成输出结果
        SpotResult result = DataConverter.buildResult(event);
        result.setData(AccountTransferResult.builder()
                .id(businessId)
                .userId(userId)
                .currency(currency)
                .amount(changeAmount)
                .result(ResultErrorEnums.SUCCESS.getCode())
                .currencyAccount(currencyAccount)
                .ledger(ledger)
                .build());
        return result;
    }


}
