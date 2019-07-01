package com.test;

public class StringResveTest {


  public static void main(String[] args) {
    String abc = "abcdefg";

    char[] array = abc.toCharArray();

    char[] resveArray = new char[array.length];
    for (int i=0;i<array.length;i++) {
      resveArray[i] = array[array.length-i-1];
    }

    String resverStr = new String(resveArray);
    System.out.println(resverStr);


  }

}
