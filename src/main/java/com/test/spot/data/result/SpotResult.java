package com.test.spot.data.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotResult {

    private Long userId;

    private Long sequence;

    private Integer resultType;

    private Object data;

    private Long receiveTime;

    private Long sendTime;

    private Long timestamp;

    private Integer version;

    private String message;

}
