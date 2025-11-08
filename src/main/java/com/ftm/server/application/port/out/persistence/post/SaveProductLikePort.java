package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.ProductLike;

@Port
public interface SaveProductLikePort {
    void saveProductLike(ProductLike productLike);
}
