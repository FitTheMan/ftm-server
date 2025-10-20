package com.ftm.server.application.port.out.cache;

import com.ftm.server.application.vo.post.UserPickPopularPostCursorVo;
import com.ftm.server.common.annotation.Port;
import java.util.List;

@Port
public interface LoadUserPickAllPopularCachePort {

    List<UserPickPopularPostCursorVo> getUserPickAllPopularPosts();
}
