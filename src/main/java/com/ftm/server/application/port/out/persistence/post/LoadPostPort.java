package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.*;
import com.ftm.server.application.vo.post.BookmarkYnWrapperVo;
import com.ftm.server.application.vo.post.PostIdAndBookmarkYnVo;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.Post;
import java.util.List;
import java.util.Optional;

@Port
public interface LoadPostPort {

    Optional<Post> loadPost(FindByIdQuery query);

    List<Post> loadPostListByUsers(FindByUserIdsQuery query);

    List<Post> loadPostsByDeleteOption(FindPostByDeleteOptionQuery query);

    List<Long> loadUserPickPopularPosts(FindUserPickPopularPostsQuery query);

    List<Long> loadUserPickBiblePosts(FindUserPickBiblePostsQuery query);

    List<Long> loadTopPostsByBookmarkCount(FindTopPostsByBookmarkCountQuery query);

    List<BookmarkYnWrapperVo> loadUserPickAllPostsByLatest(
            FindUserPickLatestPostsByCursorQuery query);

    List<PostIdAndBookmarkYnVo> loadPostIdAndBookmarkYn(FindByPostIdsAndUserQuery query);
}
