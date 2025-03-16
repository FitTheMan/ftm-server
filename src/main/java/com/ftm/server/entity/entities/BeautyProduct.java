package com.ftm.server.entity.entities;

import com.ftm.server.entity.enums.BeautyProductCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "beauty_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BeautyProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_image_link")
    private String productImageLink;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "beauty_product_category", columnDefinition = "beauty_product_category")
    private BeautyProductCategory beautyProductCategory;

    private String brand;
    private String name;
    private Double rating;

    @Column(name = "product_page_link")
    private String productPageLink;

    @Builder(access = AccessLevel.PRIVATE)
    private BeautyProduct(
            String productImageLink,
            BeautyProductCategory beautyProductCategory,
            String brand,
            String name,
            Double rating,
            String productPageLink) {
        this.productImageLink = productImageLink;
        this.beautyProductCategory = beautyProductCategory;
        this.brand = brand;
        this.name = name;
        this.rating = rating;
        this.productPageLink = productPageLink;
    }
}
