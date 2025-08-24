package com.ftm.server.application.vo.post;

import com.ftm.server.domain.enums.PostHashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostWithUserAndBookmarkCountVo {
    private final Long id;
    private final Long userId;
    private final String userName;
    private final String title;
    private final String content;
    private final PostHashtag[] hashtags;
    private final Integer viewCount;
    private final Integer likeCount;
    private final Long scrapCount;
}
