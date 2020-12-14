package com.test.spot.data.result;

import com.test.spot.data.AccountLedger;
import com.test.spot.data.CurrencyAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferResult {

    private Long id;

    private Long userId;

    private String currency;

    private BigDecimal amount;

    private Integer result;

    private String message;

    private CurrencyAccount currencyAccount;

    private AccountLedger ledger;
}
