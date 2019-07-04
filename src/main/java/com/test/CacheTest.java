package com.test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.apache.commons.lang3.StringUtils;

public class CacheTest {





    public static void main(String[] args) {

        LoadingCache<String,String> cache = CacheBuilder.newBuilder()
            // 初始容量
            .initialCapacity(10)
            // 最大容量
            .maximumSize(100)
            // 访问后多久失效
            .expireAfterAccess(1, TimeUnit.SECONDS)
            // 写入后失效
//            .expireAfterWrite(1,TimeUnit.SECONDS)
            .build(new CacheLoader<String,String>(){

                @Override
                public String load(String s) throws Exception {
                    return getFromRedis(s);
                }
            });

        String key = "key1";
        try {
            for(int i=0;i<5;i++) {
                System.out.println("-------------第"+i+"次--------------");
                String value = cache.get(key);

                Thread.sleep(500);

                System.out.println(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    public static ConcurrentHashMap<String,String> REDIS = new ConcurrentHashMap<String,String>();

    public static String getFromRedis(String key) {
        System.out.println("未命中LocalCache,进入Redis获取");
        String value = REDIS.get(key);
        if (StringUtils.isNotBlank(value)) {
            System.out.println("命中Redis");
            return value;
        }
        return getFromDB(key);
    }

    public static String getFromDB(String key) {
        System.out.println("未命中Redis,进入DB获取");
        return "value:"+key;
    }

}
