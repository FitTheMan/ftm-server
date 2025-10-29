package com.ftm.server.adapter.in.web.post.dto.response;

import com.ftm.server.application.vo.post.LoadProductsByHashTagVo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoadProductsByHashTagResponse {
    private final Long productId;
    private final String productName;
    private final String productImage;
    private final Boolean likeYn;
    private final String brand;
    private final Long recommendedCount;
    private final Long postId;

    public static LoadProductsByHashTagResponse from(LoadProductsByHashTagVo vo) {
        return new LoadProductsByHashTagResponse(
                vo.getProductId(),
                vo.getProductName(),
                vo.getProductImage(),
                vo.getLikeYn(),
                vo.getBrand(),
                vo.getRecommendedCount(),
                vo.getPostId());
    }
}
