package com.test.spot.strategy;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotExecuteStrategy<E, R, S> {

    Map<Integer, ExecuteAction> actionMap = Maps.newHashMap();

    public SpotExecuteStrategy registerAction(ExecuteAction action) {
        actionMap.put(action.getEventAction(), action);
        return this;
    }

    public R dispatch(int eventType, E event, S storage) {
        ExecuteAction<E, R, S> action = actionMap.get(eventType);
        if (Objects.isNull(action)) {

            return null;
        }
        R r = action.execute(event, storage);
        return r;
    }


}
