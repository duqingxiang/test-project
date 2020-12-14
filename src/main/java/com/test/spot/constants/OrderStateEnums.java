package com.test.spot.constants;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum OrderStateEnums {

    PREPARE(1, 0, "prepare"),
    CREATE(2, 0, "create"),
    PARTIAL_FILLED(3, 0, "partial_filled"),
    FILLED(4, 1, "filled"),
    PARTIAL_CANCELED(5, 1, "partial_canceled"),
    CANCELING(6, 1, "canceling"),
    CANCELED(7, 1, "canceled"),
    REJECTED(8, 1, "rejected"),


    ;

    private final int state;

    private final int finished;

    private final String desc;

    private static Map<Integer, OrderStateEnums> cacheMap = Maps.newHashMap();

    static {
        for (OrderStateEnums stateEnums : OrderStateEnums.values()) {
            cacheMap.put(stateEnums.getState(), stateEnums);
        }
    }

    public static OrderStateEnums find(int state) {
        return cacheMap.get(state);
    }
}
