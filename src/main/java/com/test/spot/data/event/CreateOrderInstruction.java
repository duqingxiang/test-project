package com.test.spot.data.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderInstruction {

    private String symbol;

    private Long id;

    private Long userId;

    // side: buy,sell
    private Integer side;

    // type: limit,market
    private Integer type;

    // timeInForce: gtc, ioc, fok, post only
    private Integer timeInForce;

    private BigDecimal price;

    private BigDecimal quantity;

    private BigDecimal amount;

    private Integer state;

    private String baseCurrency;

    private String quoteCurrency;

    private String feeCurrency;

    private BigDecimal takerFeeRate;

    private BigDecimal makerFeeRate;
}
