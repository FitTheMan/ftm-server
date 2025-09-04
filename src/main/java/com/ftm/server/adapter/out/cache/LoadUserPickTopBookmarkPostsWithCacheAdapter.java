package com.ftm.server.adapter.out.cache;

import static com.ftm.server.common.consts.StaticConsts.USER_PICK_TOP_BOOKMARK_POSTS_CACHE_KEY_ALL;
import static com.ftm.server.common.consts.StaticConsts.USER_PICK_TOP_BOOKMARK_POSTS_CACHE_NAME;

import com.ftm.server.application.port.out.cache.LoadUserPickTopBookmarkPostsWithCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostPort;
import com.ftm.server.application.query.FindTopPostsByBookmarkCountQuery;
import com.ftm.server.application.vo.post.PostWithIdAndAuthorVo;
import com.ftm.server.common.annotation.Adapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

@Adapter
@RequiredArgsConstructor
@CacheConfig(cacheManager = "userPickTopBookmarkPostsCacheManager")
public class LoadUserPickTopBookmarkPostsWithCacheAdapter
        implements LoadUserPickTopBookmarkPostsWithCachePort {

    private final LoadPostPort loadPostPort;

    @Override
    @Cacheable(
            cacheNames = USER_PICK_TOP_BOOKMARK_POSTS_CACHE_NAME,
            key = USER_PICK_TOP_BOOKMARK_POSTS_CACHE_KEY_ALL)
    public List<PostWithIdAndAuthorVo> getTopBookmarkPosts() {
        return execute();
    }

    @Override
    @CachePut(
            cacheNames = USER_PICK_TOP_BOOKMARK_POSTS_CACHE_NAME,
            key = USER_PICK_TOP_BOOKMARK_POSTS_CACHE_KEY_ALL)
    public List<PostWithIdAndAuthorVo> getTopBookmarkPostsCachePut() {
        return execute();
    }

    private List<PostWithIdAndAuthorVo> execute() {
        List<PostWithIdAndAuthorVo> posts =
                loadPostPort.loadTopPostsByBookmarkCount(FindTopPostsByBookmarkCountQuery.of(4));
        if (posts.isEmpty()) return List.of();
        return posts;
    }
}
