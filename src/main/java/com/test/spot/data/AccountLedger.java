package com.test.spot.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class AccountLedger {

    private Long id;

    private Long userId;

    private String currency;

    private Integer businessType;

    private Long businessId;

    private BigDecimal amount;

    private BigDecimal beforeAmount;

    private BigDecimal afterAmount;

}
