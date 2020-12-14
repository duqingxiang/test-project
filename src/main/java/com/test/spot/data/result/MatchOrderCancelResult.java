package com.test.spot.data.result;

import com.test.spot.data.CurrencyAccount;
import com.test.spot.data.SpotOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchOrderCancelResult {

    private Integer result;

    private String message;

    private Long userId;

    private Long orderId;

    private String symbol;

    private SpotOrder order;

    private CurrencyAccount account;

}
