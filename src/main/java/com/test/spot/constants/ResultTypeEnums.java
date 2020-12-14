package com.test.spot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultTypeEnums {

    ACCOUNT_TRANSFER_IN(1,"账户转入"),
    ACCOUNT_TRANSFER_OUT(2,"账户转出"),
    ORDER_CREATE(3,"下单"),
    ORDER_CANCEL(4,"撤单"),
    MATCH_ORDER_PENDING(5, "撮合-挂单成功"),
    MATCH_ORDER_FILL(6, "撮合-成交"),
    MATCH_ORDER_CANCEL(7, "撮合-撤单"),




    EXCEPTION(999,"异常"),

    ;

    private final int resultType;

    private final String resultDesc;
}
