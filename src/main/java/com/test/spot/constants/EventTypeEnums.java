package com.test.spot.constants;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum EventTypeEnums {

    ACCOUNT_TRANSFER_IN(1, "账户转入", ResultTypeEnums.ACCOUNT_TRANSFER_IN),
    ACCOUNT_TRANSFER_OUT(2, "账户转出", ResultTypeEnums.ACCOUNT_TRANSFER_OUT),
    ORDER_CREATE(3, "下单", ResultTypeEnums.ORDER_CREATE),
    ORDER_CANCEL(4, "撤单", ResultTypeEnums.ORDER_CANCEL),
    MATCH_ORDER_PENDING(5, "撮合-挂单成功", ResultTypeEnums.MATCH_ORDER_PENDING),
    MATCH_ORDER_FILL(6, "撮合-成交", ResultTypeEnums.MATCH_ORDER_FILL),
    MATCH_ORDER_CANCEL(7, "撮合-撤单", ResultTypeEnums.MATCH_ORDER_CANCEL),

    ;

    private final int eventType;

    private final String eventDesc;

    private final ResultTypeEnums result;

    private static Map<Integer, EventTypeEnums> cacheMap = Maps.newHashMap();

    static {
        for (EventTypeEnums typeEnums : EventTypeEnums.values()) {
            cacheMap.put(typeEnums.getEventType(), typeEnums);
        }
    }

    public static EventTypeEnums find(int eventType) {
        return cacheMap.get(eventType);
    }
}
