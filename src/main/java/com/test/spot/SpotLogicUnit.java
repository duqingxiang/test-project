package com.test.spot;

import com.alibaba.fastjson.JSON;
import com.test.spot.constants.ResultTypeEnums;
import com.test.spot.data.DataConverter;
import com.test.spot.data.SpotStorage;
import com.test.spot.data.event.SpotEvent;
import com.test.spot.data.result.SpotResult;
import com.test.spot.strategy.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpotLogicUnit {

    private SpotStorage storage = new SpotStorage();

    private SpotExecuteStrategy<SpotEvent, SpotResult, SpotStorage> strategy;

    public SpotLogicUnit() {
        strategy = new SpotExecuteStrategy<>();
        strategy.registerAction(new SpotAccountTransferInAction())
                .registerAction(new SpotAccountTransferOutAction())
                .registerAction(new SpotCreateOrderAction())
                .registerAction(new SpotCancelOrderAction())
        ;

    }


    public SpotResult execute(SpotEvent event) {
        SpotResult result;
        try {
            result = strategy.dispatch(event.getEventType(), event, storage);
        } catch (Exception e) {

            result = DataConverter.buildResult(event);
            result.setResultType(ResultTypeEnums.EXCEPTION.getResultType());
            result.setMessage(e.getMessage());
            log.error(" execute event exception: {}", JSON.toJSONString(event), e);
        }
        return result;
    }


}
