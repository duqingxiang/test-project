package com.test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ContractBean {

    private String symbol;

    private String rootSymbol;

    private String type;

    private String baseCurrency;

    private String quoteCurrency;

    private Long maxOrderQty;

    private BigDecimal maxPrice;

    private Integer lotSize;

    private Float tickSize;

    private Float multiplier;

    private Float initialMargin;

    private Float maintainMargin;

    private Long maxRiskLimit;

    private Long minRiskLimit;

    private Long riskStep;

    private Float makerFeeRate;

    private Float takerFeeRate;

    private BigDecimal takerFixFee;

    private BigDecimal makerFixFee;

    private Boolean isDeleverage;

    private Boolean isQuanto;

    private Boolean isInverse;

    private String markMethod;

    private String fairMethod;

    private String fundingBaseSymbol;

    private String fundingQuoteSymbol;

    private String fundingRateSymbol;

    private String indexSymbol;

    private BigDecimal volumeOf24h;

    private BigDecimal turnoverOf24h;

    private Long openInterest;

    private String status;

    private BigDecimal maxLeverage;


}