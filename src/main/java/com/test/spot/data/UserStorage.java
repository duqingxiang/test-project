package com.test.spot.data;

import com.google.common.collect.Maps;
import com.test.spot.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStorage {

    private Long userId;

    private BusinessAccount businessAccount;

    private Map<String, SpotOrder> orderMap;

    public CurrencyAccount getCurrencyAccount(String currency) {
        return this.businessAccount.getCurrencyAccount(currency);
    }

    public void updateCurrencyAccount(CurrencyAccount currencyAccount) {
        this.businessAccount.updateCurrencyAccount(currencyAccount);
    }

    public SpotOrder getOrder(String symbol, Long orderId) {
        String key = CommonUtils.generateCommonKey(symbol, orderId);
        return orderMap.get(key);
    }

    public void updateOrder(SpotOrder order) {
        String key = CommonUtils.generateCommonKey(order.getSymbol(), order.getId());
        orderMap.put(key, order);
    }

    public void removeOrder(SpotOrder order) {
        String key = CommonUtils.generateCommonKey(order.getSymbol(), order.getId());
        orderMap.remove(key);
    }


    public static UserStorage buildDefault(Long userId) {
        return UserStorage.builder()
                .userId(userId)
                .businessAccount(BusinessAccount.buildDefault(userId))
                .orderMap(Maps.newHashMap())
                .build();
    }

}
