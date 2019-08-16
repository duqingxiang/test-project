package com.test;

import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorTest {


  public static void main(String[] args) {

    final EventFactory<DataHolder> eventFactory = DataHolder::new;
    final int ringBufferSize = (1 << 16);
    final ProducerType producerType = ProducerType.SINGLE;
    final WaitStrategy waitStrategy = new BlockingWaitStrategy();

    Disruptor<DataHolder> disruptor = new Disruptor(eventFactory, ringBufferSize, new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
        return new Thread(r);
      }
    }, producerType, waitStrategy);

    EventHandler<DataHolder> handler1 = new EventHandler<DataHolder>() {
      @Override
      public void onEvent(DataHolder event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("handle1:: " + event.toString());
      }
    };
    EventHandler<DataHolder> handler2 = new EventHandler<DataHolder>() {
      @Override
      public void onEvent(DataHolder event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("handle2:: " + event.toString());
      }
    };

    disruptor.handleEventsWith(handler1, handler2);

    RingBuffer<DataHolder> ringBuffer = disruptor.start();

    for (int i = 0; i < 1000; i++) {
      long nextSequence = ringBuffer.next();
      DataHolder dataHolder = ringBuffer.get(nextSequence);
      dataHolder.setSequence(i);
      dataHolder.setData("dsfssdfsd" + i);
      dataHolder.setTimestamp(System.currentTimeMillis());
//      System.out.println("publish : " + dataHolder.toString());
      ringBuffer.publish(nextSequence);
    }

    disruptor.shutdown();


  }


  public static class DataHolder {

    private long sequence;

    private String data;

    private long timestamp;

    public long getSequence() {
      return sequence;
    }

    public void setSequence(long sequence) {
      this.sequence = sequence;
    }

    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }

    public String toString() {
      return "[sequence:" + this.sequence + " , data:" + this.data + " , timesteamp:" + this.timestamp + "]";
    }
  }

}
