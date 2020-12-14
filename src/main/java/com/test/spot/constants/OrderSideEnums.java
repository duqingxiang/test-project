package com.test.spot.constants;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum OrderSideEnums {

    BUY(1, "buy"),
    SELL(2, "sell"),

    ;

    private final int side;

    private final String desc;

    private static Map<Integer, OrderSideEnums> cacheMap = Maps.newHashMap();

    static {
        for (OrderSideEnums sideEnums : OrderSideEnums.values()) {
            cacheMap.put(sideEnums.getSide(), sideEnums);
        }
    }

    public static OrderSideEnums find(int side) {
        return cacheMap.get(side);
    }
}
