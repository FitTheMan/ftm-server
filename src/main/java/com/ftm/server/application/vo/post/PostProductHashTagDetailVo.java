package com.ftm.server.application.vo.post;

import java.util.List;
import lombok.Getter;

@Getter
public class PostProductHashTagDetailVo {

    private final PostProductCategoryVo category;
    private final List<PostProductHashTagVo> hashtags;

    private PostProductHashTagDetailVo(
            PostProductCategoryVo category, List<PostProductHashTagVo> hashtags) {
        this.category = category;
        this.hashtags = hashtags;
    }

    public static PostProductHashTagDetailVo of(
            PostProductCategoryVo category, List<PostProductHashTagVo> hashtags) {
        return new PostProductHashTagDetailVo(category, hashtags);
    }
}
