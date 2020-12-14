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
public class AccountTransferInstruction {

    private Long id;

    private Long userId;

    private String currency;

    private BigDecimal amount;
}
