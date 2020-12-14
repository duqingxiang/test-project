package com.test.spot.data;

import com.google.common.hash.HashCode;
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

    private Long userId;

    private String currency;

    private BigDecimal available;

    private BigDecimal frozen;

    public static CurrencyAccount buildDefault(Long userId, String currency) {
        return CurrencyAccount.builder()
                .userId(userId)
                .currency(currency)
                .available(BigDecimal.ZERO)
                .frozen(BigDecimal.ZERO)
                .build();
    }

    @Override
    public int hashCode() {
        return currency.hashCode() + userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
