package com.ftm.server.application.port.in.post;

import com.ftm.server.application.vo.post.TrendingUserVo;
import com.ftm.server.common.annotation.UseCase;
import java.util.List;

@UseCase
public interface LoadTrendingManUseCase {
    List<TrendingUserVo> execute();
}
