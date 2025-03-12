package com.ftm.server.entity.enums;

import lombok.Getter;

@Getter
public enum BeautyProductCategory {
    SKINCARE("스킨케어");

    private final String value;

    BeautyProductCategory(String value) {
        this.value = value;
    }
}
