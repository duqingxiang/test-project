package com.test.huobiapi;

import java.util.List;

import javax.annotation.Nullable;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class CommonWebSocketListener extends okhttp3.WebSocketListener {

  private List<String> comandList;

  private WebSocketMessageHandler messageHandler;


  public CommonWebSocketListener(List<String> comandList, WebSocketMessageHandler handler) {
    this.comandList = comandList;
    this.messageHandler = handler;
  }

  public void onOpen(WebSocket webSocket, Response response) {
    if (comandList == null || comandList.size() <= 0) {
      return;
    }

    for(String comand : comandList) {
      webSocket.send(comand);
    }
  }

  public void onMessage(WebSocket webSocket, String text) {
    messageHandler.onMessage(webSocket, text);
  }

  public void onMessage(WebSocket webSocket, ByteString bytes) {
    messageHandler.onMessage(webSocket, bytes);
  }

  public void onClosing(WebSocket webSocket, int code, String reason) {
  }

  public void onClosed(WebSocket webSocket, int code, String reason) {
  }

  public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
  }
}
