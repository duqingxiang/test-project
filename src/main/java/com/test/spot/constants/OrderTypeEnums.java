package com.test.spot.constants;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum OrderTypeEnums {

    LIMIT(1, "limit"),
    MARKET(2, "market"),

    ;

    private final int type;

    private final String desc;

    private static Map<Integer, OrderTypeEnums> cacheMap = Maps.newHashMap();

    static {
        for (OrderTypeEnums typeEnums : OrderTypeEnums.values()) {
            cacheMap.put(typeEnums.getType(), typeEnums);
        }
    }

    public static OrderTypeEnums find(int type) {
        return cacheMap.get(type);
    }
}
