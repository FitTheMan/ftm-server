package com.ftm.server.application.vo.post;

import com.ftm.server.domain.enums.PostHashtag;
import lombok.Getter;

@Getter
public class PostHashTagVo {

    private final String name;
    private final String tag;

    private PostHashTagVo(PostHashtag postHashTag) {
        this.name = postHashTag.name();
        this.tag = postHashTag.getTag();
    }

    public static PostHashTagVo of(PostHashtag postHashTag) {
        return new PostHashTagVo(postHashTag);
    }
}
