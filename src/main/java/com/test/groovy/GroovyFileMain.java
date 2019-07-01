package com.test.groovy;


import java.io.File;
import java.util.List;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import org.apache.commons.lang3.RandomUtils;

public class GroovyFileMain {

  public static void main(String[] args) {

    try {

      String filePath = "/Users/duqingxiang/IdeaProjects/test-project/src/main/java/com/test/groovy/TestGroovy.groovy";

      GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
      File sourceFile = new File(filePath);
      Class testGroovyClass = classLoader.parseClass(new GroovyCodeSource(sourceFile));
      GroovyObject instance = (GroovyObject)testGroovyClass.newInstance();//proxy


      for (int i = 0; i < 3; i++) {
        Object[] params = {RandomUtils.nextLong(), RandomUtils.nextLong(), RandomUtils.nextDouble()};
        System.out.println("=========================");
        long time = System.currentTimeMillis();
        List<SettleTest> list = (List<SettleTest>)instance.invokeMethod("hello",params);
        list.forEach(settleTest -> System.out.println(settleTest.toString()));
        System.out.println("cost time:" + (System.currentTimeMillis() - time));
      }



    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
