package com.test.kafka;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

public class ProcessorSupplierDemo implements ProcessorSupplier<String, String> {


  @Override
  public Processor<String, String> get() {
    return new KafkaProcessorDemo();
  }
}
