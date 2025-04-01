package com.ftm.server.domain.entity;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostProductImage extends BaseTime {

    private Long id;
    private Long postProductId;
    private String objectKey;

    @Builder(access = AccessLevel.PRIVATE)
    private PostProductImage(
            Long id,
            Long postProductId,
            String objectKey,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.postProductId = postProductId;
        this.objectKey = objectKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostProductImage of(
            Long id,
            Long postProductId,
            String objectKey,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return PostProductImage.builder()
                .id(id)
                .postProductId(postProductId)
                .objectKey(objectKey)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
