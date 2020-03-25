package com.test.huobiapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class HuobiMessageUtils {

  private HuobiMessageUtils() {}

  public static byte[] decode(byte[] data) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    decompress(bais, baos);
    baos.flush();
    baos.close();
    bais.close();
    return baos.toByteArray();
  }

  private static void decompress(InputStream is, OutputStream os) throws IOException {
    GZIPInputStream gis = new GZIPInputStream(is);
    int count;
    byte[] data = new byte[1024];
    while ((count = gis.read(data, 0, 1024)) != -1) {
      os.write(data, 0, count);
    }
    gis.close();
  }

  public static void await(long n) {
    try {
      Thread.sleep(n);
    } catch (InterruptedException e) {
      log.warn(e.getMessage());
    }
  }

  public static boolean pingResponse(JSONObject response, WebSocket webSocket) {
    boolean flag = false;
    String op = response.getString("op");
    if ("ping".equals(op)) {
      long ts = response.getLong("ts");
      String pong = String.format("{\"op\":\"pong\",\"ts\":%d}", ts);
      log.info("Pong:" + pong);
      webSocket.send(pong);
      flag = true;
    } else if (response.containsKey("ping")) {
      long ts = response.getLong("ping");
      String pong = String.format("{\"pong\":%d}", ts);
      log.info("Pong:" + pong);
      webSocket.send(pong);
      flag = true;
    }
    return flag;
  }

  public static String getChSymbol(String input) {
    if (StringUtils.isBlank(input)) {
      return null;
    }
    String[] fields = input.split("\\.");
    if (fields.length >= 2) {
      return fields[1];
    }
    return null;
  }

}
