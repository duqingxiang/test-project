package com.test;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduleTest {

    public static void main(String[] args) {

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);


        AtomicLong incr = new AtomicLong(1);
        scheduledExecutorService.scheduleAtFixedRate(()->{

            Long index = incr.getAndAdd(1);

            System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss")+"----> start schedule "+index);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss")+"----> end schedule "+index);
        },1000,2000, TimeUnit.MILLISECONDS);


    }
}
