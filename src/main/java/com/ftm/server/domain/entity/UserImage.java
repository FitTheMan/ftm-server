package com.ftm.server.domain.entity;

import com.ftm.server.common.consts.PropertiesHolder;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage extends BaseTime {

    private Long id;
    private Long userId;
    private String objectKey = PropertiesHolder.USER_DEFAULT_IMAGE;

    @Builder(access = AccessLevel.PRIVATE)
    private UserImage(
            Long id,
            Long userId,
            String objectKey,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.objectKey = objectKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserImage of(
            Long id,
            Long userId,
            String objectKey,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return UserImage.builder()
                .id(id)
                .userId(userId)
                .objectKey(objectKey)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static UserImage createUserImage(Long userId) {
        return UserImage.builder()
                .userId(userId)
                .objectKey(PropertiesHolder.USER_DEFAULT_IMAGE)
                .build();
    }

    public static UserImage defaultForSystem(Long userId) {
        return createUserImage(userId);
    }

    public void updateDefaultUserImage() {
        this.objectKey = PropertiesHolder.USER_DEFAULT_IMAGE;
    }

    public void updateUserImage(String objectKey) {
        this.objectKey = objectKey;
    }
}
