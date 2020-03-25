package com.test;

public class FirstTest {

  public static void main(String[] args) {

    int lines = 60 * 60 * 24;
    System.out.println("day lines: " + lines);
    double fileSize = 962.7;
    System.out.println("single file（100 lines）: " + fileSize + " kb");
    double totalSize = (lines / 100) * fileSize;
    System.out.println("day file: " + totalSize + " kb");
    System.out.println("day file: " + (totalSize / 1024) + " mb");

//    RunTask task = new RunTask();
//
//    new Thread(task).start();
//
//    while (true) {
//      if (task.getState().equals(1)) {
//        break;
//      }
//    }
//
//    System.out.println("finish");
  }

}

class RunTask implements Runnable {

  volatile Integer state = 0;

  @Override
  public void run() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    state = 1;
    System.out.println(" 赋值完成");
  }

  public Integer getState() {
    return state;
  }
}

