package com.ftm.server.application.port.in.post;

import com.ftm.server.application.query.FindUserPickLatestPostsByCursorQuery;
import com.ftm.server.application.query.FindUserPickPopularPostsByCursorQuery;
import com.ftm.server.application.vo.post.GetUserPickAllPostsLatestWithCursorVo;
import com.ftm.server.application.vo.post.GetUserPickAllPostsPopularWithCursorVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface GetUserPickPostsUseCase {

    GetUserPickAllPostsLatestWithCursorVo executeLatest(FindUserPickLatestPostsByCursorQuery query);

    GetUserPickAllPostsPopularWithCursorVo executePopular(
            FindUserPickPopularPostsByCursorQuery query);
}
