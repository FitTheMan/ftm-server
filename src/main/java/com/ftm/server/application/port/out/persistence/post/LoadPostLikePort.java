package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.vo.post.LoadPostAndUserLikeVo;
import com.ftm.server.common.annotation.Port;
import java.util.List;
import java.util.Optional;

@Port
public interface LoadPostLikePort {
    List<LoadPostAndUserLikeVo> findPostLikeByUser(Long userId, List<Long> postIds);

    LoadPostAndUserLikeVo findPostLikeByUser(Long userId, Long postId);

    Optional<Long> findOneByUserAndPost(Long userId, Long postId);
}
