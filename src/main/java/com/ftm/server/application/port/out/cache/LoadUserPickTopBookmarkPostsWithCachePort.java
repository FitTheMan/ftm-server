package com.ftm.server.application.port.out.cache;

import java.util.List;

public interface LoadUserPickTopBookmarkPostsWithCachePort {

    List<Long> getTopBookmarkPosts();

    List<Long> getTopBookmarkPostsCachePut();
}
