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
public class CancelOrderResult {

    private Long id;

    private Long userId;

    private String symbol;

    private Integer result;

    private String message;

    private CurrencyAccount currencyAccount;

    private SpotOrder order;

}
