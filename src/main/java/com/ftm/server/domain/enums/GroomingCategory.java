package com.ftm.server.domain.enums;

import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import java.util.Arrays;
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

    public static GroomingCategory from(String value) {
        return Arrays.stream(GroomingCategory.values())
                .filter(category -> category.name().equals(value.toUpperCase()))
                .findFirst()
                .orElseThrow(
                        () -> new CustomException(ErrorResponseCode.GROOMING_CATEGORY_NOT_FOUND));
    }
}
