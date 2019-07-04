package com.test;

public class FirstTest {

  public static void main(String[] args) {

    String str = "123456789";

    String newStr = new StringBuilder(str).reverse().toString();
    System.out.println("method 1: " + newStr);

    String newStr1 = new String(reverse(str.toCharArray()));
    System.out.println("method 2: " + newStr1);

    String newStr2 = new String(reverse(str.toCharArray(),str.length()));
    System.out.println("method 3: " + newStr2);
  }


  public static char[] reverse(char[] src, int size) {

    int i = size - 1;
    int j = 0;

    for (; i >= j; i--, j++) {
      char temp = src[i];
      src[i] = src[j];
      src[j] = temp;
    }
    return src;
  }

  public static char[] reverse(char[] array) {
    int arrayLength = array.length;
    char[] newArray = new char[arrayLength];

    for (int i = 0; i < arrayLength; i++) {
      newArray[arrayLength - 1 - i] = array[i];
    }
    return newArray;
  }


}
