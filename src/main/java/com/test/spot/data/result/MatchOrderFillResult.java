package com.test.spot.data.result;

import com.google.common.collect.Lists;
import com.test.spot.data.AccountLedger;
import com.test.spot.data.CurrencyAccount;
import com.test.spot.data.MatchFill;
import com.test.spot.data.SpotOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchOrderFillResult {

    private Integer result;

    private String message;

    private String symbol;

    private List<CurrencyAccount> accountList = Lists.newArrayList();

    private List<SpotOrder> orderList = Lists.newArrayList();

    private List<MatchFill> fillList = Lists.newArrayList();

    private List<AccountLedger> ledgerList = Lists.newArrayList();


    public void addAccount(CurrencyAccount account) {
        accountList.add(account);
    }

    public void addOrder(SpotOrder order) {
        orderList.add(order);
    }

    public void addFill(MatchFill fill) {
        fillList.add(fill);
    }

    public void addLedger(AccountLedger ledger) {
        ledgerList.add(ledger);
    }

    public void add(MatchOrderFillResult result) {
        if (CollectionUtils.isEmpty(result.getAccountList())) {
            accountList.addAll(result.getAccountList());
        }

        if (CollectionUtils.isEmpty(result.getOrderList())) {
            orderList.addAll(result.getOrderList());
        }

        if (CollectionUtils.isEmpty(result.getFillList())) {
            fillList.addAll(result.getFillList());
        }

        if (CollectionUtils.isEmpty(result.getLedgerList())) {
            ledgerList.addAll(result.getLedgerList());
        }
    }

    public static MatchOrderFillResult build(String symbol) {
        MatchOrderFillResult result = new MatchOrderFillResult();
        result.setSymbol(symbol);
        return result;
    }
}
