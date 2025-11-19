package com.ftm.server.application.vo.post;

import java.util.List;
import lombok.Data;

@Data
public class LoadProductsByHashTagVoWrapper {
    private final List<LoadProductsByHashTagVo> data;
    private final Boolean hasNext;
    private final Double lastScore;

    public static LoadProductsByHashTagVoWrapper of(
            List<LoadProductsByHashTagVo> data, Boolean hasNext, Double lastScore) {
        return new LoadProductsByHashTagVoWrapper(data, hasNext, lastScore);
    }
}
