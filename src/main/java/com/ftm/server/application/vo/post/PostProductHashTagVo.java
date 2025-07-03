package com.ftm.server.application.vo.post;

import com.ftm.server.domain.enums.ProductHashtag;
import lombok.Getter;

@Getter
public class PostProductHashTagVo {

    private final String name;
    private final String tag;

    private PostProductHashTagVo(ProductHashtag productHashTag) {
        this.name = productHashTag.name();
        this.tag = productHashTag.getTag();
    }

    public static PostProductHashTagVo of(ProductHashtag productHashTag) {
        return new PostProductHashTagVo(productHashTag);
    }
}
