package com.ftm.server.application.vo.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPickPopularPostCursorVo {
    private Long postId;
    private Double score;
}
