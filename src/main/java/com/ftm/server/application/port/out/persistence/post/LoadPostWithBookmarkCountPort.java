package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindPostsByCreatedDateQuery;
import com.ftm.server.application.vo.post.PostWithBookmarkCountVo;
import java.util.List;

public interface LoadPostWithBookmarkCountPort {
    List<PostWithBookmarkCountVo> loadAllPostsWithBookmarkCount(FindPostsByCreatedDateQuery query);
}
