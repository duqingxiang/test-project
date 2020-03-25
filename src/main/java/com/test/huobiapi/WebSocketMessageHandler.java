package com.test.huobiapi;

import okhttp3.WebSocket;
import okio.ByteString;

public interface WebSocketMessageHandler {

  void onMessage(WebSocket webSocket, String text);

  void onMessage(WebSocket webSocket, ByteString bytes);

}
