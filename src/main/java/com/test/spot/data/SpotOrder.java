package com.test.spot.data;

import com.test.spot.data.event.CreateOrderInstruction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotOrder {

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

    private BigDecimal finishQuantity;

    private Integer state;

    private String baseCurrency;

    private String quoteCurrency;

    private String feeCurrency;

    private BigDecimal takerFeeRate;

    private BigDecimal makerFeeRate;



    public static SpotOrder convert(CreateOrderInstruction instruction) {
        return SpotOrder.builder()
                .symbol(instruction.getSymbol())
                .id(instruction.getId())
                .userId(instruction.getUserId())
                .side(instruction.getSide())
                .type(instruction.getType())
                .timeInForce(instruction.getTimeInForce())
                .price(instruction.getPrice())
                .quantity(instruction.getQuantity())
                .amount(instruction.getAmount())
                .finishQuantity(BigDecimal.ZERO)
                .state(instruction.getState())
                .baseCurrency(instruction.getBaseCurrency())
                .quoteCurrency(instruction.getQuoteCurrency())
                .takerFeeRate(instruction.getTakerFeeRate())
                .makerFeeRate(instruction.getMakerFeeRate())
                .feeCurrency(instruction.getFeeCurrency())
                .build();
    }

}
