package com.ftm.server.application.vo.post;

import lombok.Getter;

@Getter
public class PostProductUpdateWithImageVo {

    private final Long postProductId;
    private final Long deletePostProductImageId;
    private final int imageIndex;

    private PostProductUpdateWithImageVo(
            Long postProductId, Long deletePostProductImageId, int imageIndex) {
        this.postProductId = postProductId;
        this.deletePostProductImageId = deletePostProductImageId;
        this.imageIndex = imageIndex;
    }

    public static PostProductUpdateWithImageVo of(
            Long postProductId, Long deletePostProductImageId, int imageIndex) {
        return new PostProductUpdateWithImageVo(
                postProductId, deletePostProductImageId, imageIndex);
    }
}
