package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.common.annotation.Port;

@Port
public interface DeleteProductLikePort {

    void deleteProductLike(Long productLikeId);
}
