package com.ftm.server.application.vo.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostWithBookmarkCountVo {
    private final Long postId;
    private final String title;
    private final Integer viewCount;
    private final Integer likeCount;
    private final Long scrapCount;
}
