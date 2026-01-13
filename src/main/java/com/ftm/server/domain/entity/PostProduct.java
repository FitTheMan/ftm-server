package com.ftm.server.domain.entity;

import com.ftm.server.application.command.post.SavePostProductCommand;
import com.ftm.server.application.command.post.UpdatePostProductCommand;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.enums.ProductHashtag;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private ProductHashtag[] hashtags;
    private Long recommendedCount;

    @Builder(access = AccessLevel.PRIVATE)
    private PostProduct(
            Long id,
            Long postId,
            String name,
            String brand,
            ProductHashtag[] hashtags,
            Long recommendedCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.name = name;
        this.brand = brand;
        this.hashtags = hashtags;
        this.recommendedCount = recommendedCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostProduct of(
            Long id,
            Long postId,
            String name,
            String brand,
            ProductHashtag[] hashtags,
            Long recommendedCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return PostProduct.builder()
                .id(id)
                .postId(postId)
                .name(name)
                .brand(brand)
                .hashtags(hashtags)
                .recommendedCount(recommendedCount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static PostProduct create(SavePostProductCommand command) {
        return PostProduct.builder()
                .postId(command.getPostId())
                .name(command.getName())
                .brand(command.getBrand())
                .hashtags(command.getHashtags())
                .recommendedCount(0L)
                .build();
    }

    public void update(UpdatePostProductCommand command) {
        if (command.getName() != null) this.name = command.getName();
        if (command.getBrand() != null) this.brand = command.getBrand();
        if (command.getHashtags() != null) this.hashtags = command.getHashtags();
    }

    public void validatePost(Long postId) {
        if (!Objects.equals(this.postId, postId)) {
            throw new CustomException(ErrorResponseCode.UNAUTHORIZED_POST_PRODUCT_ACCESS);
        }
    }

    public void plusRecommendedCountByOne() {
        this.recommendedCount += 1;
    }

    public void minusRecommendedCountByOne() {
        this.recommendedCount -= 1;
    }
}
