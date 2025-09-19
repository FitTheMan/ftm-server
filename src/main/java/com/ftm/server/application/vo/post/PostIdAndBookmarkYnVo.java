package com.ftm.server.application.vo.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostIdAndBookmarkYnVo {
    private final Long postId;
    private final Boolean bookmarkYn;
}
