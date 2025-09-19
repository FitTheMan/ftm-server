package com.ftm.server.adapter.in.web.post.dto.response;

import com.ftm.server.application.vo.post.GetUserPickAllPostsLatestWithCursorVo;
import com.ftm.server.application.vo.post.GetUserPickPostsVo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetUserPickPostsLatestResponse {

    private List<GetUserPickPostsVo> data;
    private LocalDateTime nextCursorDateTime; // 다음 요청을 위한 datetime
    private Boolean hasNext;

    public static GetUserPickPostsLatestResponse from(GetUserPickAllPostsLatestWithCursorVo vo) {
        return new GetUserPickPostsLatestResponse(
                vo.getPostList(), vo.getNextCursorDateTime(), vo.getHasNext());
    }
}
