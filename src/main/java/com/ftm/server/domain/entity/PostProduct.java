package com.ftm.server.domain.entity;

import com.ftm.server.domain.enums.HashTag;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostProduct extends BaseTime {

    private Long id;
    private Long postId;
    private String name;
    private String brand;
    private HashTag[] hashTags;

    @Builder(access = AccessLevel.PRIVATE)
    private PostProduct(
            Long id,
            Long postId,
            String name,
            String brand,
            HashTag[] hashTags,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.name = name;
        this.brand = brand;
        this.hashTags = hashTags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostProduct of(
            Long id,
            Long postId,
            String name,
            String brand,
            HashTag[] hashTags,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return PostProduct.builder()
                .id(id)
                .postId(postId)
                .name(name)
                .brand(brand)
                .hashTags(hashTags)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
