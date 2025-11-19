package com.ftm.server.application.vo.post;

import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.PostProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoadProductsByHashTagVo {

    private final Long productId;
    private final String productName;
    private final String brand;
    private final Long recommendedCount;
    private final Long postId;
    private final String productImage;
    private final Boolean likeYn;

    public static LoadProductsByHashTagVo from(
            PostProduct postProduct, LoadProductAndUserLikeVo vo, PostProductImage imageUrl) {
        String productImage =
                imageUrl == null ? PropertiesHolder.PRODUCT_DEFAULT_IMAGE : imageUrl.getObjectKey();
        productImage = PropertiesHolder.CDN_PATH + "/" + productImage;
        return new LoadProductsByHashTagVo(
                postProduct.getId(),
                postProduct.getName(),
                postProduct.getBrand(),
                postProduct.getRecommendedCount(),
                postProduct.getPostId(),
                productImage,
                vo.getLikeYn());
    }
}
