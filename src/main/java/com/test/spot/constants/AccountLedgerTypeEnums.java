package com.test.spot.constants;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum AccountLedgerTypeEnums {

    ACCOUNT_TRANSFER_IN(1, "账户转入"),
    ACCOUNT_TRANSFER_OUT(2, "账户转出"),
    ORDER_MATCH_FILL(3, "订单撮合成交"),
    ORDER_MATCH_FILL_FEE(4, "订单撮合成交手续费"),

    ;

    private final int ledgerType;

    private final String desc;


    private static Map<Integer, AccountLedgerTypeEnums> cacheMap = Maps.newHashMap();

    static {
        for (AccountLedgerTypeEnums typeEnums : AccountLedgerTypeEnums.values()) {
            cacheMap.put(typeEnums.getLedgerType(), typeEnums);
        }
    }

    public static AccountLedgerTypeEnums find(int ledgerType) {
        return cacheMap.get(ledgerType);
    }
}
