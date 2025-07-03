package com.ftm.server.application.vo.post;

import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.enums.PostHashtag;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostInfoVo {

    private final Long id;
    private final String title;
    private final String content;
    private final PostHashtag[] hashtags;
    private final Integer viewCount;
    private final Integer likeCount;
    private final Boolean isDeleted;
    private final LocalDateTime deletedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private PostInfoVo(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.hashtags = post.getHashtags();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.isDeleted = post.getIsDeleted();
        this.deletedAt = post.getDeletedAt();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public static PostInfoVo from(Post post) {
        return new PostInfoVo(post);
    }
}
