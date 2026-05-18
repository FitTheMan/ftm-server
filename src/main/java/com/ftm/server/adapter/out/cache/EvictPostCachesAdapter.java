package com.ftm.server.adapter.out.cache;

import static com.ftm.server.common.consts.StaticConsts.*;

import com.ftm.server.application.port.out.cache.EvictPostCachesPort;
import com.ftm.server.common.annotation.Adapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;

@Adapter
public class EvictPostCachesAdapter implements EvictPostCachesPort {

    private final CacheManager defaultCacheManager;
    private final CacheManager trendingPostsCacheManager;
    private final CacheManager userPickPopularPostsCacheManager;
    private final CacheManager userPickTopBookmarkPostsCacheManager;
    private final CacheManager userPickAllPopularPostsCacheManager;

    public EvictPostCachesAdapter(
            CacheManager defaultCacheManager,
            @Qualifier("trendingPostsCacheManager") CacheManager trendingPostsCacheManager,
            @Qualifier("userPickPopularPostsCacheManager")
                    CacheManager userPickPopularPostsCacheManager,
            @Qualifier("userPickTopBookmarkPostsCacheManager")
                    CacheManager userPickTopBookmarkPostsCacheManager,
            @Qualifier("userPickAllPopularPostsCacheManager")
                    CacheManager userPickAllPopularPostsCacheManager) {
        this.defaultCacheManager = defaultCacheManager;
        this.trendingPostsCacheManager = trendingPostsCacheManager;
        this.userPickPopularPostsCacheManager = userPickPopularPostsCacheManager;
        this.userPickTopBookmarkPostsCacheManager = userPickTopBookmarkPostsCacheManager;
        this.userPickAllPopularPostsCacheManager = userPickAllPopularPostsCacheManager;
    }

    @Override
    public void evictAllPostRankingCaches() {
        evict(defaultCacheManager, USER_PICK_BIBLE_POSTS_CACHE_NAME);
        evict(trendingPostsCacheManager, TRENDING_POSTS_CACHE_NAME);
        evict(userPickPopularPostsCacheManager, USER_PICK_POPULAR_POSTS_CACHE_NAME);
        evict(userPickTopBookmarkPostsCacheManager, USER_PICK_TOP_BOOKMARK_POSTS_CACHE_NAME);
        evict(userPickAllPopularPostsCacheManager, USER_PICK_STORY_POPULAR_POSTS_CACHE_NAME);
    }

    private void evict(CacheManager manager, String cacheName) {
        var cache = manager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
