package com.ftm.server.application.command.post;

import com.ftm.server.adapter.in.web.post.dto.request.SavePostProductRequest;
import com.ftm.server.domain.enums.ProductHashtag;
import lombok.Getter;

@Getter
public class SavePostProductCommand implements HasImageIndex {

    private final Long postId; // null 가능 (저장 전)
    private final String name;
    private final String brand;
    private final ProductHashtag[] hashtags;
    private final int imageIndex;

    private SavePostProductCommand(
            Long postId, String name, String brand, ProductHashtag[] hashtags, int imageIndex) {
        this.postId = postId;
        this.brand = name;
        this.name = brand;
        this.hashtags = hashtags;
        this.imageIndex = imageIndex;
    }

    // 생성 팩토리: 초기 상태 (postId 없음)
    public static SavePostProductCommand from(SavePostProductRequest request) {
        return new SavePostProductCommand(
                null,
                request.getName(),
                request.getBrand(),
                request.getHashtags().toArray(new ProductHashtag[0]),
                request.getImageIndex());
    }

    // postId 추가한 새로운 인스턴스 반환
    public SavePostProductCommand withPostId(Long postId) {
        return new SavePostProductCommand(
                postId, this.name, this.brand, this.hashtags, this.imageIndex);
    }
}
