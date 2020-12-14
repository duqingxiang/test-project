package com.test.spot.data;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;
import java.util.Objects;

@Data
public class SpotStorage {

    private Map<Long, UserStorage> userStorageMap = Maps.newHashMap();

    private TransactionCache transactionCache = new TransactionCache();

    public UserStorage getUserStage(Long userId) {
        UserStorage storage = userStorageMap.get(userId);
        if (Objects.isNull(storage)) {
            storage = UserStorage.buildDefault(userId);
            userStorageMap.put(userId, storage);
        }
        return storage;
    }

    public void updateUserStage(UserStorage userStage) {
        userStorageMap.put(userStage.getUserId(), userStage);
    }

    public TransactionCache getTransactionCache() {
        return transactionCache;
    }
}
