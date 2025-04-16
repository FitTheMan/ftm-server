package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.Post;

@Port
public interface SavePostPort {

    Post savePost(Post post);
}
