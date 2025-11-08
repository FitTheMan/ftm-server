package com.ftm.server.domain.entity;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class ProductLike extends BaseTime {
    private Long id;
    private Long postProduct;
    private Long user;

    public static ProductLike create(Long postProduct, Long user) {
        return ProductLike.builder().postProduct(postProduct).user(user).build();
    }

    public static ProductLike create(Long id, Long postProduct, Long user) {
        return ProductLike.builder().id(id).postProduct(postProduct).user(user).build();
    }
}
