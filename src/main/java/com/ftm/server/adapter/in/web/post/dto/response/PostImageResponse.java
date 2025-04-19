package com.ftm.server.adapter.in.web.post.dto.response;

import static com.ftm.server.common.consts.PropertiesHolder.CDN_PATH;

import com.ftm.server.domain.entity.PostImage;
import lombok.Getter;

@Getter
public class PostImageResponse {

    private final Long postImageId;
    private final String imageUrl;

    private PostImageResponse(PostImage postImage) {
        this.postImageId = postImage.getId();
        this.imageUrl = CDN_PATH + "/" + postImage.getObjectKey();
    }

    public static PostImageResponse from(PostImage postImage) {
        return new PostImageResponse(postImage);
    }
}
