package com.ftm.server.application.vo.post;

import com.ftm.server.application.vo.HashtagCategoryDetailVo;
import java.util.List;
import lombok.Getter;

@Getter
public class PostHashTagDetailVo {

    private final HashtagCategoryDetailVo category;
    private final List<PostHashTagVo> hashtags;

    private PostHashTagDetailVo(HashtagCategoryDetailVo category, List<PostHashTagVo> hashtags) {
        this.category = category;
        this.hashtags = hashtags;
    }

    public static PostHashTagDetailVo of(
            HashtagCategoryDetailVo category, List<PostHashTagVo> hashtags) {
        return new PostHashTagDetailVo(category, hashtags);
    }
}
