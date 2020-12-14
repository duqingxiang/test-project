package com.test.spot.data;

import com.google.common.collect.Maps;
import com.test.spot.utils.CommonUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class TransactionCache {

    private static float DEFAULT_MAP_LOAD_FACTOR = 0.75F;

    private Integer capacity = 10 * 10000;

    private Map<Integer, LinkedHashMap<String, Integer>> cacheMap = Maps.newHashMap();

    public boolean checkAndPutTransactionId(Integer eventType, String tag, Long transactionId) {
        LinkedHashMap<String, Integer> map = cacheMap.getOrDefault(eventType, new LinkedHashMap<String, Integer>(capacity, DEFAULT_MAP_LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > capacity;  // 容量大于capacity 时就删除
            }
        });

        String key = CommonUtils.generateCommonKey(tag, transactionId);
        boolean exist = map.containsKey(key);
        if (exist) {
            return true;
        }

        map.put(key, 1);
        cacheMap.put(eventType, map);
        return false;
    }


}
