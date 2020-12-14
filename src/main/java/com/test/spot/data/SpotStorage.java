package com.test.spot.data;

import com.google.common.collect.Maps;

import java.util.Map;

public class SpotStorage {

    private Map<Long, UserStorage> userStorageMap = Maps.newHashMap();

    private TransactionCache transactionCache = new TransactionCache();

    public UserStorage getUserStage(Long userId) {
        return userStorageMap.getOrDefault(userId, UserStorage.buildDefault(userId));
    }

    public void updateUserStage(UserStorage userStage) {
        userStorageMap.put(userStage.getUserId(), userStage);
    }

    public TransactionCache getTransactionCache() {
        return transactionCache;
    }
}
