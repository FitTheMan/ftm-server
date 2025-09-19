package com.ftm.server.application.vo.post;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPickBiblePostsVo {
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

    public static UserPickBiblePostsVo of(
            Integer ranking,
            PostWithUserAndBookmarkCountVo post,
            String imageUrl,
            List<String> hashtags,
            Boolean userBookmarkYn) {
        return new UserPickBiblePostsVo(
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
