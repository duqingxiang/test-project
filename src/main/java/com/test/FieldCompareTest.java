package com.test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class FieldCompareTest {


  public static void main(String[] args) {

    TestBean bean1 = new TestBean();
    bean1.setId(1L);
    bean1.setName("a");
    bean1.setSex("man");

    TestBean bean2 = new TestBean();
    bean2.setId(2L);
    bean2.setName("b");
    bean2.setSex("woman");

    TestBean bean3 = new TestBean();
    bean3.setId(1L);
    bean3.setName("a");
    bean3.setSex("man");


    long totalCost = 0;
    int count = 10000;
    for (int i=0;i<count;i++) {
      long startTime = System.nanoTime();
      boolean result = compareBean(bean1, bean2);
      long endTime1 = System.nanoTime();

      totalCost += (endTime1 - startTime);
    }

    System.out.println("finish avg:"+(totalCost / count));


//    boolean result1 = compareBean(bean1, bean3);
//    System.out.println("compare result:" + result1 + " cost:" + (System.nanoTime() - endTime1));


  }

  public static boolean compareBean(TestBean bean1, TestBean bean2) {

    Map<String, Object> map1 = getBeanValueMap(bean1);
    Map<String, Object> map2 = getBeanValueMap(bean2);

    boolean flag = true;
    for (Entry<String, Object> entry : map1.entrySet()) {
      String field = entry.getKey();
      Object b1Value = entry.getValue();
      Object b2Value = map2.get(field);
      if (b2Value == null || !b1Value.equals(b2Value)) {
        flag = false;
//        System.out.println("Field:" + field + " find diff: b1:" + b1Value + " b2:" + b2Value);
      }
    }

    return flag;
  }


  public static Map<String, Object> getBeanValueMap(TestBean bean) {
    Map<String, Object> map = Maps.newHashMap();
    try {
      Field[] fields = bean.getClass().getDeclaredFields();
      for (Field field : fields) {
        field.setAccessible(true);
        map.put(field.getName(), field.get(bean));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }

}


class TestBean {

  private Long id;

  private String name;

  private String sex;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }
}