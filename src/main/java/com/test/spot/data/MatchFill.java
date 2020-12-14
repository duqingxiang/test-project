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
public class MatchFill {

    private Long id;

    private String symbol;

    private Long userId;

    private Long orderId;

    private BigDecimal price;

    private BigDecimal amount;

    private Integer role;
}
