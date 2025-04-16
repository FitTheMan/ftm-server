package com.ftm.server.domain.entity;

import static com.ftm.server.common.consts.PropertiesHolder.POST_DEFAULT_IMAGE;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseTime {

    private Long id;
    private Long postId;
    private String objectKey;

    @Builder(access = AccessLevel.PRIVATE)
    private PostImage(
            Long id,
            Long postId,
            String objectKey,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.objectKey = objectKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostImage of(
            Long id,
            Long postId,
            String objectKey,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return PostImage.builder()
                .id(id)
                .postId(postId)
                .objectKey(objectKey)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static PostImage create(Long postId, String objectKey) {
        return PostImage.builder().postId(postId).objectKey(objectKey).build();
    }

    public static PostImage createDefault(Long postId) {
        return PostImage.builder().postId(postId).objectKey(POST_DEFAULT_IMAGE).build();
    }
}
