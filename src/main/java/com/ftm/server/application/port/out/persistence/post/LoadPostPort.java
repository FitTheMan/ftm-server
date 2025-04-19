package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.Post;
import java.util.Optional;

@Port
public interface LoadPostPort {

    Optional<Post> loadPost(FindByIdQuery query);
}
