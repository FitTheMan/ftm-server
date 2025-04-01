package com.ftm.server.domain.entity;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTime {

    private Long id;
    private Long userId;
    private Long postId;

    @Builder(access = AccessLevel.PRIVATE)
    private Bookmark(
            Long id, Long userId, Long postId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Bookmark of(
            Long id, Long userId, Long postId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return Bookmark.builder()
                .id(id)
                .userId(userId)
                .postId(postId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
