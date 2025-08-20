package com.ftm.server.application.port.in.user;

import com.ftm.server.application.vo.post.UserPickPopularPostsVo;
import com.ftm.server.common.annotation.UseCase;
import java.util.List;

@UseCase
public interface GetUserPickPopularPostsUseCase {
    List<UserPickPopularPostsVo> execute();
}
