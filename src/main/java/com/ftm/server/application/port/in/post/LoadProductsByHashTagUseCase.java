package com.ftm.server.application.port.in.post;

import com.ftm.server.application.vo.post.LoadProductsByHashTagVoWrapper;
import com.ftm.server.common.annotation.UseCase;
import com.ftm.server.domain.enums.ProductHashtag;
import java.util.List;

@UseCase
public interface LoadProductsByHashTagUseCase {

    LoadProductsByHashTagVoWrapper execute(
            Long userId, List<ProductHashtag> hashtagList, Integer limit, Double score);
}
