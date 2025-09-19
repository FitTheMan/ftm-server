package com.ftm.server.application.vo.post;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public class UserPickPopularPostsVo {

    private final Integer ranking;
    private final Long postId;
    private final String title;
    private final Long authorId;
    private final String authorName;
    private final Integer viewCount;
    private final Integer likeCount;
    private final Long scrapCount;
    private final String imageUrl;
    private final List<String> hashtags;
    private final Boolean userBookmarkYn;

    public static UserPickPopularPostsVo of(
            Integer ranking,
            PostWithUserAndBookmarkCountVo post,
            String imageUrl,
            List<String> hashtags,
            Boolean userBookmarkYn) {
        return new UserPickPopularPostsVo(
                ranking,
                post.getId(),
                post.getTitle(),
                post.getUserId(),
                post.getUserName(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getScrapCount(),
                imageUrl,
                hashtags,
                userBookmarkYn);
    }
}
