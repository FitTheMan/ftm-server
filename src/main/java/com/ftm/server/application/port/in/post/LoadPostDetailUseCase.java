package com.ftm.server.application.port.in.post;

import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.vo.post.PostDetailVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface LoadPostDetailUseCase {

    PostDetailVo execute(Long userId, FindByIdQuery query);
}
