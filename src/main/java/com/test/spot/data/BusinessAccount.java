package com.test.spot.data;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessAccount {

    private Long userId;

    private Map<String, CurrencyAccount> currencyAccountMap;

    public CurrencyAccount getCurrencyAccount(String currency) {
        return currencyAccountMap.getOrDefault(currency, CurrencyAccount.buildDefault(this.getUserId(), currency));
    }

    public void updateCurrencyAccount(CurrencyAccount account) {
        currencyAccountMap.put(account.getCurrency(), account);
    }

    public static BusinessAccount buildDefault(Long userId) {
        return BusinessAccount.builder()
                .userId(userId)
                .currencyAccountMap(Maps.newHashMap())
                .build();
    }

}
