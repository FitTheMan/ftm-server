package com.ftm.server.domain.entity;

import static com.ftm.server.common.consts.PropertiesHolder.PRODUCT_DEFAULT_IMAGE;

import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import java.time.LocalDateTime;
import java.util.Objects;
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

    public static PostProductImage create(Long postProductId, String objectKey) {
        return PostProductImage.builder().postProductId(postProductId).objectKey(objectKey).build();
    }

    public static PostProductImage createDefault(Long postProductId) {
        return PostProductImage.builder()
                .postProductId(postProductId)
                .objectKey(PRODUCT_DEFAULT_IMAGE)
                .build();
    }

    public void updateObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public void validateId(Long postProductImageId) {
        if (!Objects.equals(this.id, postProductImageId)) {
            throw new CustomException(ErrorResponseCode.UNAUTHORIZED_POST_PRODUCT_IMAGE_ACCESS);
        }
    }
}
