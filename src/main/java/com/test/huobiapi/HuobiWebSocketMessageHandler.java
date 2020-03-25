package com.test.huobiapi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.huobiapi.model.MarketDepth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import okio.ByteString;
import org.apache.commons.lang3.time.DateFormatUtils;

@Slf4j
public class HuobiWebSocketMessageHandler implements WebSocketMessageHandler {

  private static final String FILE_PATH = "/Users/duqingxiang/Documents/test_template/market_depth_data/market_depth_";
  private static final int WRITE_LINE_COUNT = 100;

  @Override
  public void onMessage(WebSocket webSocket, String text) {

  }

  @Override
  public void onMessage(WebSocket webSocket, ByteString bytes) {
    String message = null;
    try {
      message = new String(HuobiMessageUtils.decode(bytes.toByteArray()));
    } catch (IOException e) {
      log.error("decode message exception:", e);
      return;
    }

    JSONObject data = JSON.parseObject(message);
    if (HuobiMessageUtils.pingResponse(data, webSocket)) {
      return;
    }

//    System.out.println("origin: " + message);
    if (data.containsKey("subbed")) {
      return;
    }

    MarketDepth depth = MarketDepth.parse(data);
    String jsonString = JSON.toJSONString(depth);
//    System.out.println(jsonString.length() + " ==> " + jsonString);

    writeFile(jsonString);

  }


  static FileWriter FILE_WRITER;
  static PrintWriter POINT_WRITER;
  static AtomicLong WRITE_LINES = new AtomicLong();

  public static void writeFile(String str) {

    long line = WRITE_LINES.getAndAdd(1);
    if (line % WRITE_LINE_COUNT == 0) {
      closeFileStream();
      initFileStream();
      WRITE_LINES.set(1);
    }

    System.out.println("line: " + line + " data:" + str);
    try {
      POINT_WRITER.println(str);
      FILE_WRITER.flush();
    } catch (IOException e) {
      closeFileStream();
      e.printStackTrace();
    }
  }

  public static void initFileStream() {
    String timeString = DateFormatUtils.format(new Date(), "yyyyMMdd_HH_mm_ss");
    String filePath = FILE_PATH + timeString;

    try {
      //如果文件存在，则追加内容；如果文件不存在，则创建文件
      File f = new File(filePath);
      FILE_WRITER = new FileWriter(f, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    POINT_WRITER = new PrintWriter(FILE_WRITER);
  }

  public static void closeFileStream() {

    if (FILE_WRITER != null) {
      try {
        FILE_WRITER.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (POINT_WRITER != null) {
      try {
        POINT_WRITER.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
