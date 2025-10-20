package com.ftm.server.application.vo.post;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetUserPickAllPostsPopularWithCursorVo {

    List<GetUserPickPostsVo> postList;
    Boolean hasNext;
    private Long nextPostId;
    private Double nextScore;

    public static GetUserPickAllPostsPopularWithCursorVo of(
            List<GetUserPickPostsVo> postList, Boolean hasNext, Long nextPostId, Double nextScore) {
        return new GetUserPickAllPostsPopularWithCursorVo(postList, hasNext, nextPostId, nextScore);
    }
}
