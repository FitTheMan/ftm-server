package com.ftm.server.application.vo.post;

import static com.ftm.server.common.consts.PropertiesHolder.CDN_PATH;

import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import lombok.Getter;

@Getter
public class PostSummaryVo {

    private final Long id;
    private final String title;
    private final String createdAt;
    private final String updatedAt;
    private final String imageUrl;

    private PostSummaryVo(Post post, PostImage postImage) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.createdAt = post.getCreatedAt().toLocalDate().toString();
        this.updatedAt = post.getUpdatedAt().toLocalDate().toString();
        this.imageUrl = CDN_PATH + "/" + postImage.getObjectKey();
    }

    public static PostSummaryVo from(Post post, PostImage postImage) {
        return new PostSummaryVo(post, postImage);
    }
}
