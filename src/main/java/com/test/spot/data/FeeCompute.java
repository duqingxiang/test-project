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
public class FeeCompute {

    private String feeCurrency;

    private BigDecimal fee;

    private Boolean normal;

    private String normalFeeCurrency;

    private BigDecimal normalFee;

}
