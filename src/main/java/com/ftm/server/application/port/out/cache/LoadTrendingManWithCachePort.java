package com.ftm.server.application.port.out.cache;

import com.ftm.server.application.vo.post.TrendingUserVo;
import com.ftm.server.common.annotation.Port;
import java.util.List;

@Port
public interface LoadTrendingManWithCachePort {
    List<TrendingUserVo> loadTrendingMan();
}
