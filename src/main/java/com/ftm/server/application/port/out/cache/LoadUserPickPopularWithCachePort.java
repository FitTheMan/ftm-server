package com.ftm.server.application.port.out.cache;

import com.ftm.server.common.annotation.Port;
import java.util.List;

@Port
public interface LoadUserPickPopularWithCachePort {

    List<Long> getUserPickPopularPost();

    List<Long> getUserPickPopularPostCachePut();
}
