package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.post.LoadTrendingManUseCase;
import com.ftm.server.application.port.out.cache.LoadTrendingManWithCachePort;
import com.ftm.server.application.vo.post.TrendingUserVo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadTrendingManService implements LoadTrendingManUseCase {

    private final LoadTrendingManWithCachePort loadTrendingManWithCachePort;

    @Override
    public List<TrendingUserVo> execute() {
        return loadTrendingManWithCachePort.loadTrendingMan();
    }
}
