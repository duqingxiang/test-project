package com.test.disrupter;

public class MessageEvent {

  private String topic;

  private String data;

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public Object getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

}
