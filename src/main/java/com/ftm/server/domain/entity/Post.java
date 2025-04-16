package com.ftm.server.domain.entity;

import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.domain.enums.GroomingCategory;
import com.ftm.server.domain.enums.HashTag;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTime {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private GroomingCategory groomingCategory;
    private HashTag[] hashtags;
    private Integer viewCount;
    private Integer likeCount;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Post(
            Long id,
            Long userId,
            String title,
            String content,
            GroomingCategory groomingCategory,
            HashTag[] hashtags,
            Integer viewCount,
            Integer likeCount,
            Boolean isDeleted,
            LocalDateTime deletedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.groomingCategory = groomingCategory;
        this.hashtags = hashtags;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Post of(
            Long id,
            Long userId,
            String title,
            String content,
            GroomingCategory groomingCategory,
            HashTag[] hashtags,
            Integer viewCount,
            Integer likeCount,
            Boolean isDeleted,
            LocalDateTime deletedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return Post.builder()
                .id(id)
                .userId(userId)
                .title(title)
                .content(content)
                .groomingCategory(groomingCategory)
                .hashtags(hashtags)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .isDeleted(isDeleted)
                .deletedAt(deletedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static Post create(SavePostCommand command) {
        return Post.builder()
                .userId(command.getUserId())
                .title(command.getTitle())
                .content(command.getContent())
                .groomingCategory(command.getGroomingCategory())
                .hashtags(command.getHashTags())
                .viewCount(0)
                .likeCount(0)
                .isDeleted(false)
                .build();
    }
}
