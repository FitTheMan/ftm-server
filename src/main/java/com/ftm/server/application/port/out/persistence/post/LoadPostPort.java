package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.query.FindByUserIdsQuery;
import com.ftm.server.application.query.FindPostByDeleteOptionQuery;
import com.ftm.server.application.query.FindUserPickPopularPostsQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.Post;
import java.util.List;
import java.util.Optional;

@Port
public interface LoadPostPort {

    Optional<Post> loadPost(FindByIdQuery query);

    List<Post> loadPostListByUsers(FindByUserIdsQuery query);

    List<Post> loadPostsByDeleteOption(FindPostByDeleteOptionQuery query);

    List<Post> loadUserPickPopularPosts(FindUserPickPopularPostsQuery query);
}
