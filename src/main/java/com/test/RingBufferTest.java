package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import com.test.disrupter.MessageEvent;
import com.test.disrupter.TestBatchConsumer;
import com.test.disrupter.TestBatchEventHandler;
import com.test.disrupter.TestBatchEventProcessor;

public class RingBufferTest {

  private static final EventTranslatorTwoArg<MessageEvent, String, String> TRANSLATOR = (event, sequence, topic, data) -> {
    event.setTopic(topic);
    event.setData(data);
  };

  public static void main(String[] args) {
//    // 环形数组的容量，必须要是2的次幂
    int bufferSize = 1024;

    ExecutorService executors = Executors.newFixedThreadPool(1);


    // 开启一个RingBuffer
    final RingBuffer<MessageEvent> ringBuffer = RingBuffer.createSingleProducer(new MessageEventFactory(), bufferSize, new BlockingWaitStrategy());


    // 构建一个handler
    TestBatchEventHandler<MessageEvent> batchEventHandler = new TestBatchConsumer();

    // 批量处理的processor
    TestBatchEventProcessor<MessageEvent> batchEventProcessor =
        new TestBatchEventProcessor<>(ringBuffer,ringBuffer.newBarrier(),batchEventHandler,10);

    // 增加一个均衡器
    ringBuffer.addGatingSequences(batchEventProcessor.getSequence());


    // 开启线程——> 执行处理器
    executors.submit(batchEventProcessor);


    for (int i=0;i<10000;i++) {

      String topic = "topic_"+i;
      String data = "data_"+i;

//      System.out.println("index:"+i);
      ringBuffer.publishEvent(TRANSLATOR,topic,data);


    }


  }


}

class MessageEventFactory implements EventFactory<MessageEvent> {

  @Override
  public MessageEvent newInstance() {
    return new MessageEvent();
  }
}



