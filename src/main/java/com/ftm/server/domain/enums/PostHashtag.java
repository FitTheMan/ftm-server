package com.ftm.server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostHashtag {

    // SKINCARE
    MOISTURIZE_SKIN(HashtagCategory.SKINCARE, "피부보습"),
    SUN_CARE(HashtagCategory.SKINCARE, "선케어"),
    MASK_PACK(HashtagCategory.SKINCARE, "마스크팩"),
    CLEANSING(HashtagCategory.SKINCARE, "클렌징"),

    // PERFUME

    // HEALTH

    // MAKEUP
    SUPPLEMENT(HashtagCategory.HEALTH, "영양제"),
    PERFORMANCE_SUPPLEMENT(HashtagCategory.HEALTH, "운동 보충제"),
    DIET_MANAGEMENT(HashtagCategory.HEALTH, "식단 관리"),
    EXERCISE(HashtagCategory.HEALTH, "운동"),

    // FASHION
    TOP_CLOTHING(HashtagCategory.FASHION, "상의"),
    BOTTOM_CLOTHING(HashtagCategory.FASHION, "하의"),
    SHOES(HashtagCategory.FASHION, "신발"),
    BAG(HashtagCategory.FASHION, "가방"),
    FASHION_ACCESSORIES(HashtagCategory.FASHION, "패션 소품"),

    // SHAVING

    // HAIR_STYLING
    HAIR_LOSS_MANAGEMENT(HashtagCategory.HAIR_STYLING, "탈모 관리"),
    HAIR_DYE_AND_PERM(HashtagCategory.HAIR_STYLING, "염색/펌"),

    // BODY_CARE
    WAXING(HashtagCategory.BODYCARE, "제모/왁싱"),
    DEODORANT(HashtagCategory.BODYCARE, "데오드란트"),
    ;

    private final HashtagCategory category;
    private final String tag;
}
