package com.ftm.server.domain.enums;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductHashtag {

    // SKINCARE
    SKIN_TONER(ProductCategory.SKINCARE, "스킨/토너"),
    LOTION(ProductCategory.SKINCARE, "로션"),
    CREAM(ProductCategory.SKINCARE, "크림"),
    MASK_PACK(ProductCategory.SKINCARE, "마스크팩"),
    CLEANSER(ProductCategory.SKINCARE, "클렌저"),
    PEELING_SCRUB(ProductCategory.SKINCARE, "필링/스크럽"),
    SUNSCREEN(ProductCategory.SKINCARE, "선케어"),

    // FRAGRANCE
    FABRIC_DEODORIZER(ProductCategory.FRAGRANCE, "섬유탈취제"),
    INCENSE(ProductCategory.FRAGRANCE, "인센스"),
    PERFUME(ProductCategory.FRAGRANCE, "향수"),

    // HEALTH
    SUPPLEMENT(ProductCategory.HEALTH, "영양제"),
    PERFORMANCE_SUPPLEMENT(ProductCategory.HEALTH, "운동 보충제"),
    PROBIOTIC_SUPPLEMENT(ProductCategory.HEALTH, "유산균"),

    // MAKEUP
    TONE_LOTION(ProductCategory.MAKEUP, "톤 로션/BB"),
    CUSHION_FOUNDATION(ProductCategory.MAKEUP, "쿠션/파운데이션"),
    LIP(ProductCategory.MAKEUP, "립"),
    EYEBROW(ProductCategory.MAKEUP, "아이브로우"),
    EYESHADOW(ProductCategory.MAKEUP, "아이쉐도우"),
    MAKEUP_REMOVER(ProductCategory.MAKEUP, "리무버"),
    CONCEALER(ProductCategory.MAKEUP, "컨실러"),
    MAKEUP_BRUSH(ProductCategory.MAKEUP, "브러쉬"),

    // FASHION
    TOP_CLOTHING(ProductCategory.FASHION, "상의"),
    BOTTOM_CLOTHING(ProductCategory.FASHION, "하의"),
    OUTERWEAR(ProductCategory.FASHION, "아우터/겉옷"),
    SHOES(ProductCategory.FASHION, "신발"),
    BAG(ProductCategory.FASHION, "가방"),
    FASHION_ACCESSORIES(ProductCategory.FASHION, "패션 소품/악세서리"),

    // SHAVING
    SHAVER(ProductCategory.SHAVING, "면도기"),
    ELECTRIC_SHAVER(ProductCategory.SHAVING, "전동 면도기"),
    SHAVING_FOAM_GEL(ProductCategory.SHAVING, "쉐이빙폼/젤"),
    AFTER_SHAVE(ProductCategory.SHAVING, "애프터 쉐이브"),

    // HAIR_STYLING
    HAIR_DYE_AND_DOWNPERM(ProductCategory.HAIR_STYLING, "염색/다운펌"),
    HAIR_WAX_SPRAY(ProductCategory.HAIR_STYLING, "왁스/스프레이"),
    HAIR_OIL_TONIC_CREAM(ProductCategory.HAIR_STYLING, "오일/토닉/컬크림"),
    SHAMPOO_CONDITIONER(ProductCategory.HAIR_STYLING, "샴푸/컨디셔너"),
    HAIR_EQUIPMENT(ProductCategory.HAIR_STYLING, "헤어 기기"),

    // BODY_CARE
    BODY_WASH_LOTION(ProductCategory.BODYCARE, "바디워시/로션"),
    HAND_CARE(ProductCategory.BODYCARE, "핸드케어"),
    WAXING(ProductCategory.BODYCARE, "제모/왁싱"),
    DEODORANT(ProductCategory.BODYCARE, "데오드란트"),
    ;

    private final ProductCategory category;
    private final String tag;

    public static List<ProductHashtag> findByCategory(ProductCategory category) {
        return Arrays.stream(values())
                .filter(hashTag -> hashTag.category.equals(category))
                .toList();
    }
}
