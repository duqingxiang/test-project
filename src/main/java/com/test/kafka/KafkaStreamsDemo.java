package com.test.kafka;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.Lists;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;

public class KafkaStreamsDemo {


  private final static String TOPIC = "HelloWorld";
  private final static String OUT_TOPIC_1 = "hellow_stream_out";
  private final static String OUT_TOPIC_2 = "hellow_stream_out_2";

  public static void main(String[] args) {
    Properties config = new Properties();
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "hellow_stream_demo");
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    StreamsBuilder builder = new StreamsBuilder();
    // 注册一个stream 并订阅topic
    KStream<String, String> stream1 = builder.stream(TOPIC);

    // 转换方法1：flatMapValues
    KStream<String, String> stream2 = stream1.
        flatMapValues(new ValueMapper<String,Iterable<String>>(){

          @Override
          public Iterable<String> apply(String s) {
            List<String> list = Lists.newArrayList("flatMapValues::"+s);
            return list;
          }
        });
    stream2.to(OUT_TOPIC_1);

    // 转换方法2：transform 进行转换
    KStream<String, String> stream3 = stream1.transform(new KafkaDemoTransformSupplier());
    stream3.to(OUT_TOPIC_2);

    KafkaStreams streams = new KafkaStreams(builder.build(), config);
    streams.start();
  }

}
