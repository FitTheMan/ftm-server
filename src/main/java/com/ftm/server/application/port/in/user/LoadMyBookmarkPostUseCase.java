package com.ftm.server.application.port.in.user;

import com.ftm.server.application.query.FindBookmarksByPagingQuery;
import com.ftm.server.application.vo.post.PostPagingVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface LoadMyBookmarkPostUseCase {

    PostPagingVo execute(FindBookmarksByPagingQuery query);
}
