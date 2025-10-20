package com.ftm.server.adapter.in.web.post.dto.response;

import com.ftm.server.application.vo.post.GetUserPickAllPostsPopularWithCursorVo;
import com.ftm.server.application.vo.post.GetUserPickPostsVo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserPickPostsPopularCursorResponse {

    private final List<GetUserPickPostsVo> data;
    private final Boolean hasNext;
    private final Long lastPostId;
    private final Double lastScore;

    public static GetUserPickPostsPopularCursorResponse from(
            GetUserPickAllPostsPopularWithCursorVo vo) {
        return new GetUserPickPostsPopularCursorResponse(
                vo.getPostList(), vo.getHasNext(), vo.getNextPostId(), vo.getNextScore());
    }
}
