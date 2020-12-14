package com.test.spot.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyAccount {

    private String currency;

    private BigDecimal available;

    private BigDecimal frozen;

    public static CurrencyAccount buildDefault(String currency) {
        return CurrencyAccount.builder()
                .currency(currency)
                .available(BigDecimal.ZERO)
                .frozen(BigDecimal.ZERO)
                .build();
    }
}
