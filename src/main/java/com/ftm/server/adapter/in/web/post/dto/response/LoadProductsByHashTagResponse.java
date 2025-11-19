package com.ftm.server.adapter.in.web.post.dto.response;

import com.ftm.server.application.vo.post.LoadProductsByHashTagVo;
import com.ftm.server.application.vo.post.LoadProductsByHashTagVoWrapper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoadProductsByHashTagResponse {
    private final List<LoadProductsByHashTagVo> data;
    private final Boolean hasNext;
    private final Double lastScore;

    public static LoadProductsByHashTagResponse from(LoadProductsByHashTagVoWrapper wrapper) {
        return new LoadProductsByHashTagResponse(
                wrapper.getData(), wrapper.getHasNext(), wrapper.getLastScore());
    }
}
