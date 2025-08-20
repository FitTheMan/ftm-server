package com.ftm.server.application.vo.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostAndBookmarkCountVo {
    private final Long postId;
    private final Long bookmarkCount;
}
