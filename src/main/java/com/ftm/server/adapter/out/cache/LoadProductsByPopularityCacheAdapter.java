package com.ftm.server.adapter.out.cache;

import static com.ftm.server.common.consts.StaticConsts.*;

import com.ftm.server.application.port.out.cache.LoadProductsByPopularityCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostProductPort;
import com.ftm.server.application.vo.post.ProductIdAndScoreVo;
import com.ftm.server.common.annotation.Adapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

@Adapter
@Slf4j
@CacheConfig(cacheManager = "trendingUsersCacheManager")
@RequiredArgsConstructor
public class LoadProductsByPopularityCacheAdapter implements LoadProductsByPopularityCachePort {

    private final LoadPostProductPort loadPostProductPort;

    @Override
    @Cacheable(
            value = PRODUCTS_BY_POPULARITY_CACHE_NAME,
            key = PRODUCTS_BY_POPULARITY_CACHE_KEY_ALL)
    public List<ProductIdAndScoreVo> loadProductsByPopularity() {
        return execute();
    }

    @Override
    @CachePut(value = PRODUCTS_BY_POPULARITY_CACHE_NAME, key = PRODUCTS_BY_POPULARITY_CACHE_KEY_ALL)
    public List<ProductIdAndScoreVo> updateProductsByPopularity() {
        return execute();
    }

    public List<ProductIdAndScoreVo> execute() {
        return loadPostProductPort.loadAllProductsByPopularity();
    }
}
