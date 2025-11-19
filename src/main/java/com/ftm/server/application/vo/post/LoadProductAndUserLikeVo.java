package com.ftm.server.application.vo.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoadProductAndUserLikeVo {
    private final Long productId;
    private final Long userId;
    private final Boolean likeYn;
}
