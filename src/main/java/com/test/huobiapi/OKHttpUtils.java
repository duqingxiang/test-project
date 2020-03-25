package com.test.huobiapi;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

@Slf4j
public class OKHttpUtils {

  private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
      .pingInterval(20, TimeUnit.SECONDS)
      .readTimeout(10, TimeUnit.SECONDS)
      .addInterceptor(new Interceptor() {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {

          Request request = chain.request();
          HttpUrl httpUrl = request.url();

          Long startTime = System.currentTimeMillis();
          Response response = chain.proceed(request);

          int responseCode = response.code();

          Long endTime = System.currentTimeMillis();
          Long costTime = endTime - startTime;
          log.info(" OKHttpInterceptor code:{} method:{} host:{} path:{} cost:{} startTime:{} endTime:{}",
              responseCode, request.method(), httpUrl.host(), httpUrl.encodedPath(), costTime, startTime, endTime);

          return response;
        }
      })
      .build();


  public static String executeSync(Request request) throws Exception {
    String str = null;
    try (Response response = okHttpClient.newCall(request).execute()) {

      if (response.code() != 200) {
        String url = request.url().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);
        jsonObject.put("code", response.code());
        jsonObject.put("message", Objects.requireNonNull(response.body()).string());
        log.debug("{}", jsonObject.toJSONString());
        throw new Exception(" request http status code error." + jsonObject.toJSONString());
      }

      str = Objects.requireNonNull(response.body()).string();
    } catch (IOException e) {
      log.error(e.getMessage());
      throw e;
    }
    return str;
  }

  public static WebSocket createWebSocket(Request request, WebSocketListener listener) {
    return okHttpClient.newWebSocket(request, listener);
  }

}
