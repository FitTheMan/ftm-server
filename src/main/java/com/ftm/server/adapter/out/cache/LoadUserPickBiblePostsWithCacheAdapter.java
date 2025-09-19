package com.ftm.server.adapter.out.cache;

import static com.ftm.server.common.consts.StaticConsts.*;

import com.ftm.server.application.port.out.cache.LoadUserPickBiblePostsWithCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostPort;
import com.ftm.server.application.query.FindUserPickBiblePostsQuery;
import com.ftm.server.common.annotation.Adapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

@Adapter
@RequiredArgsConstructor
public class LoadUserPickBiblePostsWithCacheAdapter implements LoadUserPickBiblePostsWithCachePort {

    private final LoadPostPort loadPostPort;

    @Override
    @Cacheable(
            cacheNames = USER_PICK_BIBLE_POSTS_CACHE_NAME,
            key = USER_PICK_BIBLE_POSTS_CACHE_KEY_ALL)
    public List<Long> getUserPickBiblePost() {
        return execute();
    }

    @Override
    @CachePut(
            cacheNames = USER_PICK_BIBLE_POSTS_CACHE_NAME,
            key = USER_PICK_BIBLE_POSTS_CACHE_KEY_ALL)
    public List<Long> getUserPickBiblePostCachePut() {
        return execute();
    }

    public List<Long> execute() {
        // 최근 1개월 상위 4개 post id를 조회

        List<Long> postList =
                loadPostPort.loadUserPickBiblePosts(FindUserPickBiblePostsQuery.of(4));

        if (postList.isEmpty()) return List.of();

        return postList;
    }
}
