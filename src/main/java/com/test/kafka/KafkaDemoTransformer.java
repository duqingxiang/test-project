package com.test.kafka;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

public class KafkaDemoTransformer implements Transformer<String,String, KeyValue<String, String>> {

  private ProcessorContext processorContext;
  @Override
  public void init(ProcessorContext processorContext) {
    this.processorContext = processorContext;
  }

  @Override
  public KeyValue<String, String> transform(String key, String value) {
    String str = "transfer::"+value;
//    processorContext.forward(key,str);

    return new KeyValue<>(key,str);
  }

  @Override
  public void close() {

  }

}
