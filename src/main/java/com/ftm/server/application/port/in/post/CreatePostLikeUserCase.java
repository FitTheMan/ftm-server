package com.ftm.server.application.port.in.post;

import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface CreatePostLikeUserCase {
    Boolean execute(Long userId, Long postId);
}
