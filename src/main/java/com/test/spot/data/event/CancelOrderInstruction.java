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
public class CancelOrderInstruction {

    private Long id;

    private String symbol;

    private Long userId;

}
