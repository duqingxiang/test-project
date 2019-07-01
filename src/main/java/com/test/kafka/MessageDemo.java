package com.test.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MessageDemo {

  public static void main(String[] args) {
    String topic = "message_demo";
    String serverHost = "127.0.0.1:9092";


    Scanner scanner = new Scanner(System.in);
    System.out.print("请输入姓名:");
    String name = scanner.nextLine();
    System.out.println("你姓名是：" + name);

    // 开启消费者
    createConsumer(serverHost, topic);

    // 开启生产者
    createProducer(scanner,serverHost,topic,name);
  }


  public static void createProducer(Scanner scanner, String serverHost,String topic, String name){
    Properties properties = new Properties();
    properties.put("bootstrap.servers", serverHost);
    properties.put("acks", "all");
    properties.put("retries", 0);
    properties.put("batch.size", 16384);
    properties.put("linger.ms", 1);
    properties.put("buffer.memory", 33554);
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    Producer<String, String> producer = null;
    try {

      producer = new KafkaProducer<String, String>(properties);

      while (true) {
        String msg = scanner.nextLine();
        if (StringUtils.isBlank(msg)) {
          continue;
        }
        String key =  name+"";
        producer.send(new ProducerRecord<String, String>(topic, key, msg));
      }
    } catch (Exception e) {
      e.printStackTrace();

    } finally {
      producer.close();
    }
  }



  public static void createConsumer(final String serverHost,final String topic){

    new Thread(()->{
      Properties properties = new Properties();
      properties.put("bootstrap.servers", serverHost);
      properties.put("group.id", "group-1");
      properties.put("enable.auto.commit", "true");
      properties.put("auto.commit.interval.ms", "1000");
      properties.put("auto.offset.reset", "earliest");
      properties.put("session.timeout.ms", "30000");
      properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
      properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

      KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
      kafkaConsumer.subscribe(Arrays.asList(topic));

      while (true) {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
          System.out.println("name= "+record.key()+", msg:" + record.value());
        }
      }
    }).start();



  }



}
