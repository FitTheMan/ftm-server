package com.ftm.server.application.vo.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoadPostAndUserLikeVo {
    private final Long postId;
    private final Long userId;
    private final Boolean likeYn;
}
