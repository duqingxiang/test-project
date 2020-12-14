package com.test.spot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  SystemUserEnums {

    SPOT_FEE_USER(1L,"现货手续费账户"),

    ;

    private Long userId;

    private String desc;
}
