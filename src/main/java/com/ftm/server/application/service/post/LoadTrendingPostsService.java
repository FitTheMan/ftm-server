package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.post.LoadTrendingPostsUseCase;
import com.ftm.server.application.port.out.cache.LoadTrendingPostsWithCachePort;
import com.ftm.server.application.vo.post.TrendingPostVo;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoadTrendingPostsService implements LoadTrendingPostsUseCase {

    private final LoadTrendingPostsWithCachePort loadTrendingPostsWithCachePort;

    @Override
    @Transactional(readOnly = true)
    public List<TrendingPostVo> execute() {
        return loadTrendingPostsWithCachePort.loadTrendingPosts();
    }
}
