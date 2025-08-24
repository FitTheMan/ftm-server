package com.ftm.server.application.port.out.cache;

import com.ftm.server.application.vo.post.PostWithIdAndAuthorVo;
import java.util.List;

public interface LoadUserPickBiblePostsWithCachePort {

    List<PostWithIdAndAuthorVo> getUserPickBiblePost();

    List<PostWithIdAndAuthorVo> getUserPickBiblePostCachePut();
}
