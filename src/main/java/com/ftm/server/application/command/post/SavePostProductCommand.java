package com.ftm.server.application.command.post;

import com.ftm.server.adapter.in.web.post.dto.request.SavePostProductRequest;
import com.ftm.server.domain.enums.HashTag;
import lombok.Getter;

@Getter
public class SavePostProductCommand implements HasImageIndex {

    private final Long postId; // null 가능 (저장 전)
    private final String name;
    private final String brand;
    private final HashTag[] hashTags;
    private final int imageIndex;

    private SavePostProductCommand(
            Long postId, String name, String brand, HashTag[] hashTags, int imageIndex) {
        this.postId = postId;
        this.brand = name;
        this.name = brand;
        this.hashTags = hashTags;
        this.imageIndex = imageIndex;
    }

    // 생성 팩토리: 초기 상태 (postId 없음)
    public static SavePostProductCommand from(SavePostProductRequest request) {
        return new SavePostProductCommand(
                null,
                request.getName(),
                request.getBrand(),
                request.getHashtags().toArray(new HashTag[0]),
                request.getImageIndex());
    }

    // postId 추가한 새로운 인스턴스 반환
    public SavePostProductCommand withPostId(Long postId) {
        return new SavePostProductCommand(
                postId, this.name, this.brand, this.hashTags, this.imageIndex);
    }
}
