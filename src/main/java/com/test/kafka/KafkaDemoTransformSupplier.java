package com.test.kafka;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;

public class KafkaDemoTransformSupplier implements TransformerSupplier<String, String, KeyValue<String,String>> {


  @Override
  public Transformer<String, String, KeyValue<String, String>> get() {
    return new KafkaDemoTransformer();
  }
}
