package com.ftm.server.entity.enums;

import lombok.Getter;

@Getter
public enum HashTag {
    PERFUME("향수", GroomingCategory.HYGIENE);

    private final String value;
    private final GroomingCategory groomingCategory;

    HashTag(String value, GroomingCategory groomingCategory) {
        this.value = value;
        this.groomingCategory = groomingCategory;
    }
}
