package com.test.disrupter;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class TestBatchConsumer implements TestBatchEventHandler<MessageEvent> {

  @Override
  public void onEvent(List<MessageEvent> eventList) throws Exception {

    System.out.println("<=========start handler "+eventList.size()+"=========>");

    for (MessageEvent event : eventList) {
      System.out.println(JSON.toJSONString(event));
    }


  }
}
