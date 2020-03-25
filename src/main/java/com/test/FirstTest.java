package com.test;

public class FirstTest {

  public static void main(String[] args) {

    RunTask task = new RunTask();

    new Thread(task).start();

    while (true) {
      if (task.getState().equals(1)) {
        break;
      }
    }

    System.out.println("finish");
  }

}

class RunTask implements Runnable{

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

  public Integer getState(){
    return state;
  }
}

