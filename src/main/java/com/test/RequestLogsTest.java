package com.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.collect.Lists;

public class RequestLogsTest {


  public static void main(String[] args) {

    String filePath = "/Users/duqingxiang/Documents/doc/read.text";
    List<String> logList = readFile(filePath);
    for (String log : logList) {
//      System.out.println(log);
      boolean rangeFlag = isOutOfRange(log,"timestamp",20);
      if (rangeFlag) {
        System.out.println(log);
      }

    }

    System.out.println(" read line:" + logList.size());
  }


  public static boolean isOutOfRange(String str,String pathKey, int range) {

    String[] array = str.split(" ");

    if (array.length < 12) {
      return false;
    }

    String pathStr = array[11];
    if (pathStr.indexOf(pathKey) <= -1) {
      return false;
    }

    System.out.println(str);
    String costStr = array[12];
    String[] costArr = costStr.split(":");
    if (costArr.length < 2) {
      return false;
    }
    int value = Integer.valueOf(costArr[1]);

    return value > range;
  }


  public static List<String> readFile(String filePath) {

    List<String> list = Lists.newArrayList();
    try {

      //使用缓冲区的方法将数据读入到缓冲区中
      BufferedReader br = new BufferedReader(
          new InputStreamReader(new FileInputStream(filePath))); ////使用缓冲区的方法将数据读入到缓冲区中
      String s = br.readLine(); //定义行数

      while (s != null) //确定行数
      {
        list.add(s);
        s = br.readLine();
      }
      br.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

}
