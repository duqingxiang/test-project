package com.test.disrupter;

import java.util.List;

public interface TestBatchEventHandler<T> {
  void onEvent(List<T> eventList) throws Exception;
}
