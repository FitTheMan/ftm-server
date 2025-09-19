package com.ftm.server.application.port.out.cache;

import java.util.List;

public interface LoadUserPickBiblePostsWithCachePort {

    List<Long> getUserPickBiblePost();

    List<Long> getUserPickBiblePostCachePut();
}
