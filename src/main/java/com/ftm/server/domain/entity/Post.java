package com.ftm.server.domain.entity;

import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.application.command.post.UpdatePostCommand;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.enums.PostHashtag;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private PostHashtag[] hashtags;
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
            PostHashtag[] hashtags,
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
            PostHashtag[] hashtags,
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
                .hashtags(command.getHashtags())
                .viewCount(0)
                .likeCount(0)
                .isDeleted(false)
                .build();
    }

    public void update(UpdatePostCommand command) {
        if (command.getTitle() != null) this.title = command.getTitle();
        if (command.getContent() != null) this.content = command.getContent();
        if (command.getHashtags() != null) this.hashtags = command.getHashtags();
    }

    public void updateUserId(Long userId) {
        this.userId = userId;
    }

    public void updateViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void updateIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void updateDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void validateWriter(Long userId) {
        if (!Objects.equals(this.userId, userId)) {
            throw new CustomException(ErrorResponseCode.UNAUTHORIZED_POST_ACCESS);
        }
    }

    public void validateDeleted() {
        if (this.isDeleted && this.deletedAt != null) {
            throw new CustomException(ErrorResponseCode.POST_NOT_FOUND);
        }
    }
}
