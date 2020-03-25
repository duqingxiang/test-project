package com.test.huobiapi;

import java.util.List;

import com.google.common.collect.Lists;

import okhttp3.Request;

public class HuobiMarketTest {

  public static final String URL = "wss://api.huobi.pro/ws";

  public static void main(String[] args) {

    Request websocketRequest = new Request.Builder().url(URL).build();

    List<String> comandList = Lists.newArrayList("{\"sub\": \"market.btcusdt.depth.step0\"}");


    CommonWebSocketListener listener = new CommonWebSocketListener(comandList,new HuobiWebSocketMessageHandler());
    OKHttpUtils.createWebSocket(websocketRequest,listener);





  }

}
