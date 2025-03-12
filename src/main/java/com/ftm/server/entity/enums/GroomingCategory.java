package com.ftm.server.entity.enums;

import lombok.Getter;

@Getter
public enum GroomingCategory {
    BEAUTY("뷰티"),
    HAIR("헤어"),
    FASHION("패션"),
    WORKOUT("운동"),
    HYGIENE("위생");

    private final String value;

    GroomingCategory(String value) {
        this.value = value;
    }
}
