package com.ftm.server.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLike extends BaseTime {
    private Long id;
    private Long postProduct;
    private Long user;
}
