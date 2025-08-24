package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindBookmarkCountByPostIdsQuery;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindPostsByCreatedDateQuery;
import com.ftm.server.application.vo.post.PostAndBookmarkCountVo;
import com.ftm.server.application.vo.post.PostWithBookmarkCountVo;
import com.ftm.server.application.vo.post.PostWithUserAndBookmarkCountVo;
import com.ftm.server.application.vo.post.UserWithPostCountVo;
import com.ftm.server.common.annotation.Port;
import java.util.List;

@Port
public interface LoadPostWithBookmarkCountPort {
    List<PostWithBookmarkCountVo> loadAllPostsWithBookmarkCount(FindPostsByCreatedDateQuery query);

    List<UserWithPostCountVo> loadAllPostsWithUserAndBookmarkCount(
            FindPostsByCreatedDateQuery query);

    List<PostAndBookmarkCountVo> getPostAndBookmarkCount(FindBookmarkCountByPostIdsQuery query);

    List<PostWithUserAndBookmarkCountVo> loadPostWithUserAndBookmarkCount(FindByIdsQuery query);
}
