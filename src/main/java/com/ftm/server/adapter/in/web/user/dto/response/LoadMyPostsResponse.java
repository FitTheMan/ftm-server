package com.ftm.server.adapter.in.web.user.dto.response;

import com.ftm.server.application.vo.post.PostPagingVo;
import com.ftm.server.application.vo.post.PostSummaryVo;
import java.util.List;
import lombok.Getter;

@Getter
public class LoadMyPostsResponse {

    private final List<PostSummaryVo> items;
    private final Boolean hasNext;

    private LoadMyPostsResponse(PostPagingVo postPagingVo) {
        this.items = postPagingVo.getItems();
        this.hasNext = postPagingVo.getHasNext();
    }

    public static LoadMyPostsResponse from(PostPagingVo postPagingVo) {
        return new LoadMyPostsResponse(postPagingVo);
    }
}
