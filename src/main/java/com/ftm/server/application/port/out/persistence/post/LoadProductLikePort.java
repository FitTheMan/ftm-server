package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.common.annotation.Port;
import java.util.Optional;

@Port
public interface LoadProductLikePort {

    Optional<Long> findOneByUserAndProduct(Long userId, Long postProductId);
}
