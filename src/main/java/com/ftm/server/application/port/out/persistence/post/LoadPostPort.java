package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.*;
import com.ftm.server.application.vo.post.PostWithIdAndAuthorVo;
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

    List<PostWithIdAndAuthorVo> loadUserPickBiblePosts(FindUserPickBiblePostsQuery query);
}
