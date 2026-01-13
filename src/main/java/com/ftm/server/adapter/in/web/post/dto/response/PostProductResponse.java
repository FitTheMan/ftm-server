package com.ftm.server.adapter.in.web.post.dto.response;

import com.ftm.server.application.vo.post.PostProductDetailVo;
import com.ftm.server.domain.enums.ProductHashtag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public class PostProductResponse {

    private final Long postProductId;
    private final String name;
    private final String brand;
    private final Long recommendedCount;
    private final List<String> hashtags;
    private final PostProductImageResponse postProductImage;

    private PostProductResponse(PostProductDetailVo postProductDetailVo) {
        this.postProductId = postProductDetailVo.getPostProductId();
        this.name = postProductDetailVo.getName();
        this.brand = postProductDetailVo.getBrand();
        this.recommendedCount = postProductDetailVo.getRecommendedCount();
        this.hashtags =
                postProductDetailVo.getHashtags() == null
                                || postProductDetailVo.getHashtags().length == 0
                        ? new ArrayList<>()
                        : Arrays.stream(postProductDetailVo.getHashtags())
                                .map(ProductHashtag::getTag)
                                .toList();
        this.postProductImage =
                postProductDetailVo.getPostProductImage() == null
                        ? null
                        : PostProductImageResponse.from(postProductDetailVo.getPostProductImage());
    }

    public static PostProductResponse from(PostProductDetailVo postProductDetailVo) {
        return new PostProductResponse(postProductDetailVo);
    }
}
