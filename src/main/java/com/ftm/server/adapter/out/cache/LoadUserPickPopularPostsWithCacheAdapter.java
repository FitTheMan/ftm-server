package com.ftm.server.adapter.out.cache;

import static com.ftm.server.common.consts.StaticConsts.USER_PICK_POPULAR_POSTS_CACHE_KEY_ALL;
import static com.ftm.server.common.consts.StaticConsts.USER_PICK_POPULAR_POSTS_CACHE_NAME;

import com.ftm.server.application.port.out.cache.LoadUserPickPopularWithCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostPort;
import com.ftm.server.application.query.FindUserPickPopularPostsQuery;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.domain.entity.Post;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

@RequiredArgsConstructor
@Adapter
@Slf4j
@CacheConfig(cacheManager = "userPickPopularPostsCacheManager")
public class LoadUserPickPopularPostsWithCacheAdapter implements LoadUserPickPopularWithCachePort {

    private final LoadPostPort loadPostPort;

    @Override
    @CachePut(
            cacheNames = USER_PICK_POPULAR_POSTS_CACHE_NAME,
            key = USER_PICK_POPULAR_POSTS_CACHE_KEY_ALL)
    public List<Post> getUserPickPopularPostCachePut() {
        return execute();
    }

    @Override
    @Cacheable(
            cacheNames = USER_PICK_POPULAR_POSTS_CACHE_NAME,
            key = USER_PICK_POPULAR_POSTS_CACHE_KEY_ALL)
    public List<Post> getUserPickPopularPost() {
        return execute();
    }

    public List<Post> execute() {
        // 최근 1개월 상위 4개 post id를 조회
        LocalDateTime since = LocalDate.now().minusMonths(1).atStartOfDay();
        List<Post> postList =
                loadPostPort.loadUserPickPopularPosts(FindUserPickPopularPostsQuery.of(since, 4));

        if (postList.isEmpty()) return List.of();

        return postList;
    }
}
