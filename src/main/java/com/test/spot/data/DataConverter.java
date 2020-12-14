package com.test.spot.data;

import com.test.spot.constants.EventTypeEnums;
import com.test.spot.data.event.SpotEvent;
import com.test.spot.data.result.SpotResult;

public class DataConverter {


    public static SpotResult buildResult(SpotEvent event) {
        EventTypeEnums eventType = EventTypeEnums.find(event.getEventType());
        return SpotResult.builder()
                .userId(event.getUserId())
                .resultType(eventType.getResult().getResultType())
                .sequence(event.getSequence())
                .receiveTime(event.getReceiveTime())
                .timestamp(event.getTimestamp())
                .version(event.getVersion())
                .build();
    }
}
