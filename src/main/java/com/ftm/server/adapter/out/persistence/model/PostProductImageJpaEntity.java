package com.ftm.server.adapter.out.persistence.model;

import com.ftm.server.domain.entity.PostProductImage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_product_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostProductImageJpaEntity extends BaseTimeJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_product_id")
    private PostProductJpaEntity postProduct;

    private String objectKey;

    @Builder(access = AccessLevel.PRIVATE)
    private PostProductImageJpaEntity(PostProductJpaEntity postProduct, String objectKey) {
        this.postProduct = postProduct;
        this.objectKey = objectKey;
    }

    public static PostProductImageJpaEntity from(
            PostProductImage postProductImage, PostProductJpaEntity postProductJpaEntity) {
        return PostProductImageJpaEntity.builder()
                .postProduct(postProductJpaEntity)
                .objectKey(postProductImage.getObjectKey())
                .build();
    }

    public void updatePostProductImageForDomainEntity(PostProductImage postProductImage) {
        this.objectKey = postProductImage.getObjectKey();
    }
}
