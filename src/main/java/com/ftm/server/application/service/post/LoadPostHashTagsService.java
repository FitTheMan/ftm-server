package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.post.LoadPostHashTagsUseCase;
import com.ftm.server.application.vo.HashtagCategoryDetailVo;
import com.ftm.server.application.vo.post.PostHashTagDetailVo;
import com.ftm.server.application.vo.post.PostHashTagVo;
import com.ftm.server.domain.enums.HashtagCategory;
import com.ftm.server.domain.enums.PostHashtag;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LoadPostHashTagsService implements LoadPostHashTagsUseCase {

    @Override
    public List<PostHashTagDetailVo> execute() {
        Map<HashtagCategory, List<PostHashtag>> hashtagGroup =
                Arrays.stream(PostHashtag.values())
                        .collect(Collectors.groupingBy(PostHashtag::getCategory));

        return Arrays.stream(HashtagCategory.values())
                .map(
                        category -> {
                            HashtagCategoryDetailVo categoryDetailVo =
                                    HashtagCategoryDetailVo.of(category);
                            List<PostHashTagVo> hashTagVos =
                                    hashtagGroup.getOrDefault(category, List.of()).stream()
                                            .map(PostHashTagVo::of)
                                            .toList();

                            return PostHashTagDetailVo.of(categoryDetailVo, hashTagVos);
                        })
                .toList();
    }
}
