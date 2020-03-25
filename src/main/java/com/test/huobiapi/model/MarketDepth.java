package com.test.huobiapi.model;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.test.huobiapi.HuobiMessageUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketDepth {

  private String symbol;

  private Long timestamp;

  private Long version;

  private List<MarketDepthLevel> asks;

  private List<MarketDepthLevel> bids;

  public static MarketDepth parse(JSONObject json) {
    Long timestamp = json.getLong("ts");
    Long version = json.getLong("version");
    String ch = json.getString("ch");

    JSONObject data = json.getJSONObject("tick");
    return MarketDepth.builder()
        .symbol(HuobiMessageUtils.getChSymbol(ch))
        .timestamp(timestamp)
        .version(version)
        .bids(MarketDepthLevel.parseArray(data.getJSONArray("bids")))
        .asks(MarketDepthLevel.parseArray(data.getJSONArray("asks")))
        .build();
  }

}
