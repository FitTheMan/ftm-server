package com.ftm.server.application.vo;

import com.ftm.server.domain.enums.HashtagCategory;
import lombok.Getter;

@Getter
public class HashtagCategoryDetailVo {

    private final String name;
    private final String label;

    private HashtagCategoryDetailVo(HashtagCategory hashtagCategory) {
        this.name = hashtagCategory.name();
        this.label = hashtagCategory.getLabel();
    }

    public static HashtagCategoryDetailVo of(HashtagCategory hashtagCategory) {
        return new HashtagCategoryDetailVo(hashtagCategory);
    }
}
