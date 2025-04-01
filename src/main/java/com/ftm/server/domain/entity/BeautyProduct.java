package com.ftm.server.domain.entity;

import com.ftm.server.domain.enums.BeautyProductCategory;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BeautyProduct extends BaseTime {

    private Long id;
    private String productImageLink;
    private BeautyProductCategory beautyProductCategory;
    private String brand;
    private String name;
    private Double rating;
    private String productPageLink;

    @Builder(access = AccessLevel.PRIVATE)
    private BeautyProduct(
            Long id,
            String productImageLink,
            BeautyProductCategory beautyProductCategory,
            String brand,
            String name,
            Double rating,
            String productPageLink,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.productImageLink = productImageLink;
        this.beautyProductCategory = beautyProductCategory;
        this.brand = brand;
        this.name = name;
        this.rating = rating;
        this.productPageLink = productPageLink;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BeautyProduct of(
            Long id,
            String productImageLink,
            BeautyProductCategory beautyProductCategory,
            String brand,
            String name,
            Double rating,
            String productPageLink,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return BeautyProduct.builder()
                .id(id)
                .productImageLink(productImageLink)
                .beautyProductCategory(beautyProductCategory)
                .brand(brand)
                .name(name)
                .rating(rating)
                .productPageLink(productPageLink)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
