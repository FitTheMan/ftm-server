package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindPostsByCreatedDateQuery;
import com.ftm.server.application.vo.post.PostWithBookmarkCountVo;
import com.ftm.server.application.vo.post.PostWithUserAndBookmarkCountVo;
import com.ftm.server.application.vo.post.UserWithPostCountVo;
import java.util.List;

public interface PostWithBookmarkCustomRepository {

    List<PostWithBookmarkCountVo> findAllPostsWithBookmarkCount(FindPostsByCreatedDateQuery query);

    List<UserWithPostCountVo> findAllPostsWithUserAndBookmarkCount(
            FindPostsByCreatedDateQuery query);

    List<PostWithUserAndBookmarkCountVo> findAllPostsWithUserAndBookmarkCount(FindByIdsQuery query);
}
