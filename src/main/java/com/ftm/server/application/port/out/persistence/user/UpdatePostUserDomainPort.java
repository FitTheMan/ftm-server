package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.Post;
import java.util.List;

@Port
public interface UpdatePostUserDomainPort {
    void updatePostListBySystemUser(List<Post> postList);
}
