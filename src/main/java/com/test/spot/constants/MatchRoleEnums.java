package com.test.spot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  MatchRoleEnums {

    TAKER(1,"taker"),
    MAKER(2,"maker"),

    ;


    private int role;

    private String desc;
}
