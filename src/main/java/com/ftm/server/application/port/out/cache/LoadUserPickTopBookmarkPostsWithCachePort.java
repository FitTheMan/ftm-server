package com.ftm.server.application.port.out.cache;

import com.ftm.server.application.vo.post.PostWithIdAndAuthorVo;
import java.util.List;

public interface LoadUserPickTopBookmarkPostsWithCachePort {

    List<PostWithIdAndAuthorVo> getTopBookmarkPosts();

    List<PostWithIdAndAuthorVo> getTopBookmarkPostsCachePut();
}
