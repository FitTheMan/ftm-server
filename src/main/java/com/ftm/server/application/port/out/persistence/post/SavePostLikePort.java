package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.PostLike;

@Port
public interface SavePostLikePort {
    void savePostLike(PostLike postLike);
}
