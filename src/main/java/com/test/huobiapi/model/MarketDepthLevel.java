package com.test.huobiapi.model;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketDepthLevel {

  private BigDecimal price;

  private BigDecimal size;


  public static MarketDepthLevel parse(JSONArray array) {
    return MarketDepthLevel.builder()
        .price(array.getBigDecimal(0))
        .size(array.getBigDecimal(1))
        .build();
  }

  public static List<MarketDepthLevel> parseArray(JSONArray array) {
    List<MarketDepthLevel> list = Lists.newArrayList();
    if (array == null || array.size() <= 0) {
      return list;
    }

    for (int i = 0; i < array.size(); i++) {
      list.add(MarketDepthLevel.parse(array.getJSONArray(i)));
    }

    return list;
  }
}
