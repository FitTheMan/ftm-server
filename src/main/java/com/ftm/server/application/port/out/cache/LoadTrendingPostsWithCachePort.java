package com.ftm.server.application.port.out.cache;

import com.ftm.server.application.vo.post.TrendingPostVo;
import java.util.List;

public interface LoadTrendingPostsWithCachePort {

    List<TrendingPostVo> loadTrendingPosts();
}
