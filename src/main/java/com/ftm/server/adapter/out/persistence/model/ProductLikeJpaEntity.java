package com.ftm.server.adapter.out.persistence.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLikeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_product_id")
    private PostProductJpaEntity postProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductLikeJpaEntity(PostProductJpaEntity postProduct, UserJpaEntity user) {
        this.postProduct = postProduct;
        this.user = user;
    }

    public static ProductLikeJpaEntity from(
            PostProductJpaEntity postProductJpaEntity, UserJpaEntity userJpaEntity) {
        return ProductLikeJpaEntity.builder()
                .postProduct(postProductJpaEntity)
                .user(userJpaEntity)
                .build();
    }
}
