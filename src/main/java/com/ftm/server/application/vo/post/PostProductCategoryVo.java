package com.ftm.server.application.vo.post;

import com.ftm.server.domain.enums.ProductCategory;
import lombok.Getter;

@Getter
public class PostProductCategoryVo {

    private final String name;
    private final String label;

    private PostProductCategoryVo(ProductCategory productCategory) {
        this.name = productCategory.name();
        this.label = productCategory.getLabel();
    }

    public static PostProductCategoryVo of(ProductCategory productCategory) {
        return new PostProductCategoryVo(productCategory);
    }
}
