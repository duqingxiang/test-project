package com.test.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.Lists;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaConsumerTest {

  public static void main(String[] args) {

    List<String> topicList = Lists.newArrayList();
    topicList.add("HelloWorld");
    topicList.add("hellow_stream_out");
    topicList.add("hellow_stream_out_2");
    topicList.add("hellow_stream_out_3");

    for (String topic : topicList) {
      new Thread(startConsumer(topic)).start();
    }

  }


  public static Runnable startConsumer(final String topic) {
    return new Runnable() {
      @Override
      public void run() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "127.0.0.1:9092");
        properties.put("group.id", "group-1");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topic));

        long index = 0;
        while (true) {
          ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
          if (records.isEmpty()) {
            continue;
          }
          long time = System.currentTimeMillis();
          System.out.println(" start time:" + time);
          for (ConsumerRecord<String, String> record : records) {
            if (index == 0) {
              index = record.offset();
            } else if (record.offset() == (index + 1)) {
              index = record.offset();
            } else {
              System.out.println(" 不连续的消费 " + record.offset() + "==>" + index);
              int d = 1 / 0;
            }

            System.out .println("topic = " + record.topic() + ", "
                + "partition = " + record.partition() + ","
                + "offset = " + record.offset() + ","
                + " key = " + record.key() + ","
                + " value = " + record.value());
          }

          System.out.println(" "+topic+"::: batch execute time:" + (System.currentTimeMillis() - time) + " size:" + records.count());
        }


      }
    }

        ;
  }


}
