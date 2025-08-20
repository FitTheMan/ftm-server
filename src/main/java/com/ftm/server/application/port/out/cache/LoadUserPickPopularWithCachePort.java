package com.ftm.server.application.port.out.cache;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.Post;
import java.util.List;

@Port
public interface LoadUserPickPopularWithCachePort {

    List<Post> getUserPickPopularPost();

    List<Post> getUserPickPopularPostCachePut();
}
