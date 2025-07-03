package com.ftm.server.application.service.post.product;

import com.ftm.server.application.port.in.post.LoadPostProductHashTagsUseCase;
import com.ftm.server.application.vo.post.PostProductCategoryVo;
import com.ftm.server.application.vo.post.PostProductHashTagDetailVo;
import com.ftm.server.application.vo.post.PostProductHashTagVo;
import com.ftm.server.domain.enums.ProductCategory;
import com.ftm.server.domain.enums.ProductHashtag;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadPostProductHashTagsService implements LoadPostProductHashTagsUseCase {

    @Override
    public List<PostProductHashTagDetailVo> execute() {
        Map<ProductCategory, List<ProductHashtag>> hashtagGroup =
                Arrays.stream(ProductHashtag.values())
                        .collect(Collectors.groupingBy(ProductHashtag::getCategory));

        return hashtagGroup.entrySet().stream()
                .map(
                        entry -> {
                            PostProductCategoryVo category =
                                    PostProductCategoryVo.of(entry.getKey());
                            List<PostProductHashTagVo> hashtags =
                                    entry.getValue().stream()
                                            .map(PostProductHashTagVo::of)
                                            .toList();

                            return PostProductHashTagDetailVo.of(category, hashtags);
                        })
                .toList();
    }
}
