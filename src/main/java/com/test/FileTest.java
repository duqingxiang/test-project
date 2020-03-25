package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.lang3.RandomUtils;

public class FileTest {

  public static final String FILE_PATH = "/Users/duqingxiang/Documents/test1.txt";

  public static int COUNTS =  1 * 10000;

  public static void main(String[] args) {

    wirteFile();

//    try {
//
//      int index = 10000;
//
//      long time1 = System.currentTimeMillis();
//
//      //使用缓冲区的方法将数据读入到缓冲区中
//      BufferedReader br = new BufferedReader(
//          new InputStreamReader(new FileInputStream(FILE_PATH))); ////使用缓冲区的方法将数据读入到缓冲区中
//      String s = br.readLine(); //定义行数
//      int lines = 0;
//      while (s != null) //确定行数
//      {
//        lines++;
//        s = br.readLine();
//
//        if (index == lines) {
//          System.out.println("cost time:"+(System.currentTimeMillis()-time1)+" value:"+s);
//        }
//
//      }
//      br.close();
//
//      System.out.println(" total lines:" + lines + " cost time:" + (System.currentTimeMillis() - time1));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//

  }


  public static void wirteFile() {
    FileWriter fw = null;
    try {
      //如果文件存在，则追加内容；如果文件不存在，则创建文件
      File f = new File(FILE_PATH);
      fw = new FileWriter(f, true);
    } catch (IOException e) {
      e.printStackTrace();
    }

    long time1 = System.currentTimeMillis();
    PrintWriter pw = new PrintWriter(fw);
    for (int i = 0; i < COUNTS; i++) {
      long ran = RandomUtils.nextLong(0, 100000000);
      pw.println(ran);
    }
    long time2 = System.currentTimeMillis();
    System.out.println("each cost time :" + (time2 - time1));
    pw.flush();
    System.out.println("flush cost time :" + (System.currentTimeMillis() - time2));

    try {
      fw.flush();
      pw.close();
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
