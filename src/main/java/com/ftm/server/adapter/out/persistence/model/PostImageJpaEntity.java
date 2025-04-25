package com.ftm.server.adapter.out.persistence.model;

import com.ftm.server.domain.entity.PostImage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageJpaEntity extends BaseTimeJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostJpaEntity post;

    private String objectKey;

    @Builder(access = AccessLevel.PRIVATE)
    private PostImageJpaEntity(PostJpaEntity post, String objectKey) {
        this.post = post;
        this.objectKey = objectKey;
    }

    public static PostImageJpaEntity from(PostImage postImage, PostJpaEntity postJpaEntity) {
        return PostImageJpaEntity.builder()
                .post(postJpaEntity)
                .objectKey(postImage.getObjectKey())
                .build();
    }

    public void updatePostImageForDomainEntity(PostImage postImage) {
        this.objectKey = objectKey;
    }
}
