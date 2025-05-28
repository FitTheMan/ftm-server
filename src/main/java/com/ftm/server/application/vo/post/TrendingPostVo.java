package com.ftm.server.application.vo.post;

import lombok.Data;

@Data
public class TrendingPostVo {

    private final Long postId;
    private final Integer rank;
    private final String title;
    private final Integer viewCount;
    private final Integer likeCount;
    private final Long scrapCount;
    private final String imageUrl;

    public static TrendingPostVo from(PostWithBookmarkCountVo vo, Integer rank, String imageUrl) {
        return new TrendingPostVo(
                vo.getPostId(),
                rank,
                vo.getTitle(),
                vo.getViewCount(),
                vo.getLikeCount(),
                vo.getScrapCount(),
                imageUrl);
    }
}
