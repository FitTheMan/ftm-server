package com.ftm.server.adapter.in.web.post.dto.response;

import com.ftm.server.application.vo.post.PostProductHashTagDetailVo;
import java.util.List;
import lombok.Getter;

@Getter
public class LoadPostProductHashTagsResponse {

    private final List<PostProductHashTagDetailVo> results;

    private LoadPostProductHashTagsResponse(List<PostProductHashTagDetailVo> results) {
        this.results = results;
    }

    public static LoadPostProductHashTagsResponse of(List<PostProductHashTagDetailVo> results) {
        return new LoadPostProductHashTagsResponse(results);
    }
}
