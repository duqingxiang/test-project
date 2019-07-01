package com.test.kafka;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

public class KafkaProcessorDemo implements Processor<String,String> {


  private ProcessorContext processorContext;

  @Override
  public void init(ProcessorContext processorContext) {
    this.processorContext = processorContext;
  }

  @Override
  public void process(String key, String value) {
    String str = "process::::: key=>"+key+" value=>"+value;
    processorContext.forward(key,str);
    System.out.println(str);
  }

  @Override
  public void close() {

  }
}
