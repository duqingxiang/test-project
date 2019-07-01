package com.test.groovy;


import java.util.List;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class GroovyShellMain {

  public static void main(String[] args) {

    StringBuilder sb = new StringBuilder();
    sb.append("import com.test.groovy.SettleTest;");

    sb.append(" Object hello(from,to,amount){");
    sb.append(" println(\" groovy script execute \");");
    sb.append(" def s1 = new SettleTest(from,1,amount);");
    sb.append(" def s2 = new SettleTest(to,2,amount);");
    sb.append(" List list = new ArrayList();");
    sb.append(" list.add(s1);");
    sb.append(" list.add(s2);");
    sb.append(" return list;");
    sb.append(" }");
    try {

      Object[] params = {123, 456, 33};

      GroovyShell shell = new GroovyShell();
      Script script = shell.parse(sb.toString());

      System.out.println("===========script=============");
      System.out.println(sb.toString());
      System.out.println("===========script=============");

      for (int i = 0; i < 3; i++) {
        System.out.println("=========================");
        long time = System.currentTimeMillis();
        Object object = script.invokeMethod("hello", params);
        List<SettleTest> list = (List<SettleTest>) object;
        list.forEach(settleTest -> System.out.println(settleTest.toString()));
        System.out.println("cost time:" + (System.currentTimeMillis() - time));
      }

      //重写main方法,默认执行

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
