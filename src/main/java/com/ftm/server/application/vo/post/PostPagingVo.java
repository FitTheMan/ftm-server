package com.ftm.server.application.vo.post;

import java.util.List;
import lombok.Getter;

@Getter
public class PostPagingVo {

    private final List<PostSummaryVo> items;
    private final Boolean hasNext;

    private PostPagingVo(List<PostSummaryVo> items, Boolean hasNext) {
        this.items = items;
        this.hasNext = hasNext;
    }

    public static PostPagingVo from(List<PostSummaryVo> items, Boolean hasNext) {
        return new PostPagingVo(items, hasNext);
    }
}
