package com.ftm.server.adapter.out.cache;

import static com.ftm.server.common.consts.StaticConsts.*;

import com.ftm.server.application.port.out.cache.LoadUserPickAllPopularCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostPort;
import com.ftm.server.application.vo.post.*;
import com.ftm.server.common.annotation.Adapter;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

@Adapter
@RequiredArgsConstructor
@CacheConfig(cacheManager = "userPickAllPopularPostsCacheManager")
public class LoadUserPickAllLoadUserPickAllPopularCacheAdapter
        implements LoadUserPickAllPopularCachePort {

    private final LoadPostPort loadPostPort;

    @Override
    @Cacheable(
            value = USER_PICK_STORY_POPULAR_POSTS_CACHE_NAME,
            key = USER_PICK_STORY_POPULAR_POSTS_CACHE_KEY_ALL)
    public List<UserPickPopularPostCursorVo> getUserPickAllPopularPosts() {
        return loadPostPort.loadUserPickAllPostsByPopular();
    }
}
