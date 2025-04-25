package com.ftm.server.application.vo.post;

import lombok.Getter;

@Getter
public class PostProductSaveWithImageVo {

    private final Long postProductId;
    private final int imageIndex;

    private PostProductSaveWithImageVo(Long postProductId, int imageIndex) {
        this.postProductId = postProductId;
        this.imageIndex = imageIndex;
    }

    public static PostProductSaveWithImageVo of(Long postProductId, int imageIndex) {
        return new PostProductSaveWithImageVo(postProductId, imageIndex);
    }
}
