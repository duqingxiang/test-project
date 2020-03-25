package com.test.disrupter;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestConsumer implements EventHandler<MessageEvent> {

  @Override
  public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {

    System.out.println("seq:" + sequence+" event:"+event.getTopic());

  }
}
