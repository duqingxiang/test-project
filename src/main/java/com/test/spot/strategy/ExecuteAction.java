package com.test.spot.strategy;

public interface ExecuteAction<E, R, S> {

    default int getEventAction() {
        return -1;
    }

    R execute(E event, S storage);

}
