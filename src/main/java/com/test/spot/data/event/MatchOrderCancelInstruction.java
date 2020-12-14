package com.test.spot.data.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchOrderCancelInstruction {

    private Long userId;

    private String symbol;

    private Long orderId;

}
