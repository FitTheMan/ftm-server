package com.ftm.server.application.vo.post;

import com.ftm.server.domain.entity.Post;
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

    public static UserPickPopularPostsVo of(
            Integer ranking,
            Post post,
            String authorName,
            Long scrapCount,
            String imageUrl,
            List<String> hashtags) {
        return new UserPickPopularPostsVo(
                ranking,
                post.getId(),
                post.getTitle(),
                post.getUserId(),
                authorName,
                post.getViewCount(),
                post.getLikeCount(),
                scrapCount,
                imageUrl,
                hashtags);
    }
}
