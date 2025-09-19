package com.ftm.server.application.port.in.post;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.post.UserPickBiblePostsVo;
import com.ftm.server.common.annotation.UseCase;
import java.util.List;

@UseCase
public interface LoadUserPickBiblePostsUseCase {
    List<UserPickBiblePostsVo> execute(FindByUserIdQuery query);
}
