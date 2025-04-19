package com.ftm.server.adapter.in.web.post.dto.response;

import static com.ftm.server.common.consts.PropertiesHolder.CDN_PATH;

import com.ftm.server.domain.entity.PostProductImage;
import lombok.Getter;

@Getter
public class PostProductImageResponse {

    private final Long postProductImageId;
    private final String imageUrl;

    private PostProductImageResponse(PostProductImage postProductImage) {
        this.postProductImageId = postProductImage.getId();
        this.imageUrl = CDN_PATH + "/" + postProductImage.getObjectKey();
    }

    public static PostProductImageResponse from(PostProductImage postProductImage) {
        return new PostProductImageResponse(postProductImage);
    }
}
