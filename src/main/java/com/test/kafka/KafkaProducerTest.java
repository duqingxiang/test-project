package com.test.kafka;

import java.util.Properties;

import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerTest {

  public static void main(String[] args) {
    for (int f = 0; f < 1; f++) {
      final int index = f;
      new Thread(() -> {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "127.0.0.1:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = null;
        try {

          String topic = "HelloWorld";
          producer = new KafkaProducer<String, String>(properties);
          for (int i = 3000; i < 3010; i++) {
            String key = RandomUtils.nextInt(0, i) + "";
            String msg = "Message " + key;
            producer.send(new ProducerRecord<String, String>(topic, key, msg));
            System.out.println("Send: "+index+" " + key + "==>" + msg);
          }

        } catch (Exception e) {
          e.printStackTrace();

        } finally {
          producer.close();
        }
      }).start();

    }
  }


}
