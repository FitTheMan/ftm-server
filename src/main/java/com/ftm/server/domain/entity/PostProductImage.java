package com.ftm.server.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_product_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostProductImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_product_id")
    private PostProduct postProduct;

    private String objectKey;

    @Builder(access = AccessLevel.PRIVATE)
    private PostProductImage(PostProduct postProduct, String objectKey) {
        this.postProduct = postProduct;
        this.objectKey = objectKey;
    }
}
