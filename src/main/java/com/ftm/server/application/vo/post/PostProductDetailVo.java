package com.ftm.server.application.vo.post;

import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.PostProductImage;
import com.ftm.server.domain.enums.ProductHashtag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class PostProductDetailVo {

    private final Long postProductId;
    private final String name;
    private final String brand;
    private final ProductHashtag[] hashtags;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final PostProductImage postProductImage;
    private final Long recommendedCount;

    private PostProductDetailVo(PostProduct postProduct, PostProductImage postProductImage) {
        this.postProductId = postProduct.getId();
        this.name = postProduct.getName();
        this.brand = postProduct.getBrand();
        this.hashtags = postProduct.getHashtags();
        this.createdAt = postProduct.getCreatedAt();
        this.updatedAt = postProduct.getUpdatedAt();
        this.postProductImage = postProductImage;
        this.recommendedCount = postProduct.getRecommendedCount();
    }

    public static List<PostProductDetailVo> listFrom(
            List<PostProduct> postProducts, Map<Long, PostProductImage> postProductImageMap) {
        return postProducts.stream()
                .map(postProduct -> from(postProduct, postProductImageMap.get(postProduct.getId())))
                .toList();
    }

    public static PostProductDetailVo from(
            PostProduct postProduct, PostProductImage postProductImage) {
        return new PostProductDetailVo(postProduct, postProductImage);
    }
}
