package com.ftm.server.adapter.in.web.post.dto.response;

import com.ftm.server.application.vo.post.PostHashTagDetailVo;
import java.util.List;
import lombok.Getter;

@Getter
public class LoadPostHashTagsResponse {

    private final List<PostHashTagDetailVo> results;

    private LoadPostHashTagsResponse(List<PostHashTagDetailVo> results) {
        this.results = results;
    }

    public static LoadPostHashTagsResponse of(List<PostHashTagDetailVo> results) {
        return new LoadPostHashTagsResponse(results);
    }
}
