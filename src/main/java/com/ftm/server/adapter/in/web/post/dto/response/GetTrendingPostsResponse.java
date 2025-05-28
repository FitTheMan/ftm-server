package com.ftm.server.adapter.in.web.post.dto.response;

import com.ftm.server.application.vo.post.TrendingPostVo;
import com.ftm.server.common.consts.PropertiesHolder;
import lombok.Data;

@Data
public class GetTrendingPostsResponse {
    private final Long postId;
    private final Integer ranking;
    private final String title;
    private final Integer viewCount;
    private final Integer likeCount;
    private final Long scrapCount;
    private final String imageUrl;

    public static GetTrendingPostsResponse from(TrendingPostVo vo) {
        String imageUrl =
                vo.getImageUrl() == null ? PropertiesHolder.POST_DEFAULT_IMAGE : vo.getImageUrl();
        return new GetTrendingPostsResponse(
                vo.getPostId(),
                vo.getRank(),
                vo.getTitle(),
                vo.getViewCount(),
                vo.getLikeCount(),
                vo.getScrapCount(),
                PropertiesHolder.CDN_PATH + "/" + imageUrl);
    }
}
