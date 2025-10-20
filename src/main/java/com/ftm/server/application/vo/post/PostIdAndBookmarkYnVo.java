package com.ftm.server.application.vo.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostIdAndBookmarkYnVo {
    private final Long postId;
    private final Boolean bookmarkYn;

    public static PostIdAndBookmarkYnVo of(Long postId, Boolean bookmarkYn) {
        return new PostIdAndBookmarkYnVo(postId, bookmarkYn);
    }
}
