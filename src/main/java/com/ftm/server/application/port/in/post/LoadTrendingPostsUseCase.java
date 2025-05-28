package com.ftm.server.application.port.in.post;

import com.ftm.server.application.vo.post.TrendingPostVo;
import com.ftm.server.common.annotation.UseCase;
import java.util.List;

@UseCase
public interface LoadTrendingPostsUseCase {
    List<TrendingPostVo> execute();
}
