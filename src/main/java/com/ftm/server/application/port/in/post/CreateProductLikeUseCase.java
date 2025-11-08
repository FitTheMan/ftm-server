package com.ftm.server.application.port.in.post;

import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface CreateProductLikeUseCase {

    Boolean execute(Long userId, Long productId);
}
