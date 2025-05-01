package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.domain.entity.Post;
import java.util.List;

public interface LoadPostUserDomainPort {

    List<Post> loadPostListByUser(FindByUserIdQuery query);
}
