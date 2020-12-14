package com.test.spot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultErrorEnums {

    SUCCESS(0, "success"),
    // 通用类 100000 ~ 199999
    ENGINE_TRANSACTION_DUPLICATE(100000,"engine transaction duplicate"),

    // 账户类200000 ~ 209999
    ACCOUNT_AVAILABLE_NOT_ENOUGH(200000, "account available not enough. current available : %s"),


    // 订单类210000 ~ 219999
    ORDER_DUPLICATE(210000, "user order transaction duplicate"),
    UNSUPPORTED_ORDER_TYPE(210001, "unsupported order type"),
    UNSUPPORTED_ORDER_SIDE(210002, "unsupported order side"),
    ORDER_NOT_EXIST(210003, "order not exist"),
    ORDER_STATE_ERROR(210004, "order state error"),



    ;


    private final int code;

    private final String message;

    public String getMessage() {
        return this.message;
    }

    public String getMessage(String... args) {
        return String.format(this.message, args);
    }
}
