package com.ftm.server.adapter.in.web.post.dto.response;

import com.ftm.server.application.vo.post.UserPickBiblePostsVo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoadUserPickBiblePostsResponse {
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

    public static LoadUserPickBiblePostsResponse from(UserPickBiblePostsVo vo) {
        return new LoadUserPickBiblePostsResponse(
                vo.getRanking(),
                vo.getPostId(),
                vo.getTitle(),
                vo.getAuthorId(),
                vo.getAuthorName(),
                vo.getViewCount(),
                vo.getLikeCount(),
                vo.getScrapCount(),
                vo.getImageUrl(),
                vo.getHashtags(),
                vo.getUserBookmarkYn());
    }
}
