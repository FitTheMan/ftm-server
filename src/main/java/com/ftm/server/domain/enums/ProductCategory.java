package com.ftm.server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategory {
    SKINCARE(HashtagCategory.SKINCARE, "스킨케어"),
    FRAGRANCE(HashtagCategory.PERFUME, "프레그런스"),
    HEALTH(HashtagCategory.HEALTH, "건강"),
    MAKEUP(HashtagCategory.MAKEUP, "메이크업"),
    FASHION(HashtagCategory.FASHION, "패션"),
    SHAVING(HashtagCategory.SHAVING, "면도"),
    HAIR_STYLING(HashtagCategory.HAIR_STYLING, "헤어 스타일링"),
    BODYCARE(HashtagCategory.BODYCARE, "바디케어"),
    ;

    private final HashtagCategory hashtagCategory;
    private final String label;
}
