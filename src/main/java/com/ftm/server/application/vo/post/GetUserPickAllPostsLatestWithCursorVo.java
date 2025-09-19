package com.ftm.server.application.vo.post;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserPickAllPostsLatestWithCursorVo {

    List<GetUserPickPostsVo> postList;
    Boolean hasNext;
    LocalDateTime nextCursorDateTime;

    public static GetUserPickAllPostsLatestWithCursorVo of(
            List<GetUserPickPostsVo> postList, Boolean hasNext, LocalDateTime nextCursorDateTime) {
        return new GetUserPickAllPostsLatestWithCursorVo(postList, hasNext, nextCursorDateTime);
    }
}
