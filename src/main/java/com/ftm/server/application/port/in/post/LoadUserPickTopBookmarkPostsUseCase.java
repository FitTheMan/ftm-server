package com.ftm.server.application.port.in.post;

import com.ftm.server.application.vo.post.LoadUserPickTopBookmarkPostsVo;
import java.util.List;

public interface LoadUserPickTopBookmarkPostsUseCase {
    List<LoadUserPickTopBookmarkPostsVo> execute();
}
