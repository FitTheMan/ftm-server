package com.ftm.server.adapter.out.persistence.model;

import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.enums.HashTag;
import io.hypersistence.utils.hibernate.type.array.EnumArrayType;
import io.hypersistence.utils.hibernate.type.array.internal.AbstractArrayType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "post_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostProductJpaEntity extends BaseTimeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostJpaEntity post;

    @Column(nullable = false)
    private String name;

    private String brand;

    @Type(
            value = EnumArrayType.class,
            parameters =
                    @org.hibernate.annotations.Parameter(
                            name = AbstractArrayType.SQL_ARRAY_TYPE,
                            value = "hashtag"))
    @Column(name = "hashtags", columnDefinition = "hashtag[]")
    private HashTag[] hashtags;

    @Builder(access = AccessLevel.PRIVATE)
    private PostProductJpaEntity(
            PostJpaEntity post, String name, String brand, HashTag[] hashtags) {
        this.post = post;
        this.name = name;
        this.brand = brand;
        this.hashtags = hashtags;
    }

    public static PostProductJpaEntity from(PostProduct postProduct, PostJpaEntity postJpaEntity) {
        return PostProductJpaEntity.builder()
                .post(postJpaEntity)
                .name(postProduct.getName())
                .brand(postProduct.getBrand())
                .hashtags(postProduct.getHashTags())
                .build();
    }
}
