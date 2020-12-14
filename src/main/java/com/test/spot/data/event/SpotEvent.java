package com.test.spot.data.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotEvent {

    private Long userId;

    private Long sequence;

    private Integer eventType;

    private Long receiveTime;

    private Object data;

    private Long timestamp;

    private Integer version;

}
