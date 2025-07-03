package com.ftm.server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HashtagCategory {
    SKINCARE("스킨케어"),
    PERFUME("향수"),
    HEALTH("건강"),
    MAKEUP("메이크업"),
    FASHION("패션"),
    SHAVING("면도ㆍ쉐이빙"),
    HAIR_STYLING("헤어 스타일링"),
    BODYCARE("바디케어"),
    ;

    private final String label;
}
