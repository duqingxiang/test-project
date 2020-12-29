package com.test;

import com.google.common.collect.Maps;
import com.test.huobiapi.OKHttpUtils;
import okhttp3.Request;

import java.util.Map;
import java.util.concurrent.*;

public class TestMulitThread {


    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {

        initHttp();
        System.out.println("=========================");
        methodA();
        System.out.println("=========================");
        methodB();

    }

    public static void methodA() {

        Map<Long, String> costMap = Maps.newTreeMap();

        // 创建计数器
        final CountDownLatch latch = new CountDownLatch(3);

        // 开启三个线程，分别获取三家的时间
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Long startTime = System.currentTimeMillis();
                    getOKExTime();
                    costMap.put((System.currentTimeMillis() - startTime), "ok");
                } catch (Exception e) {

                } finally {
                    latch.countDown();
                }
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Long startTime = System.currentTimeMillis();
                    getHuobiTime();
                    costMap.put((System.currentTimeMillis() - startTime), "huobi");
                } catch (Exception e) {

                } finally {
                    latch.countDown();
                }
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Long startTime = System.currentTimeMillis();
                    getBinanceTime();
                    costMap.put((System.currentTimeMillis() - startTime), "binance");
                } catch (Exception e) {

                } finally {
                    latch.countDown();
                }

            }
        });

        try {
            // 等待计数器完成
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Long, String> entry : costMap.entrySet()) {
            System.out.println(entry.getValue() + " ==> " + entry.getKey());
        }

    }

    public static void methodB() {

        Map<String,Future<Long>> futureMap = Maps.newHashMap();

        // 创建三个future线程
        futureMap.put("ok",executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Long startTime = System.currentTimeMillis();
                getOKExTime();
                return (System.currentTimeMillis() - startTime);
            }
        }));

        futureMap.put("huobi",executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Long startTime = System.currentTimeMillis();
                getHuobiTime();
                return (System.currentTimeMillis() - startTime);
            }
        }));

        futureMap.put("binance",executorService.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Long startTime = System.currentTimeMillis();
                getBinanceTime();
                return (System.currentTimeMillis() - startTime);
            }
        }));

        // 阻塞获取future的结果，并放在map中排序
        Map<Long, String> costMap = Maps.newTreeMap();
        for (Map.Entry<String,Future<Long>> futureEntry : futureMap.entrySet()) {
            String exchange = futureEntry.getKey();
            try {
                Long cost = futureEntry.getValue().get();
                costMap.put(cost,exchange);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        // 输出结果
        for (Map.Entry<Long, String> entry : costMap.entrySet()) {
            System.out.println(entry.getValue() + " ==> " + entry.getKey());
        }


    }

    public static void initHttp() {
        System.out.println(getHuobiTime());
        System.out.println(getOKExTime());
        System.out.println(getBinanceTime());
    }


    public static String getHuobiTime() {
        Request request = new Request.Builder().url("https://api.huobi.pro/v1/common/timestamp").build();
        try {
            return OKHttpUtils.executeSync(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getOKExTime() {
        Request request = new Request.Builder().url("https://www.okexcn.com/api/general/v3/time").build();
        try {
            return OKHttpUtils.executeSync(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBinanceTime() {
        Request request = new Request.Builder().url("https://api3.binance.com/api/v1/time").build();
        try {
            return OKHttpUtils.executeSync(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

