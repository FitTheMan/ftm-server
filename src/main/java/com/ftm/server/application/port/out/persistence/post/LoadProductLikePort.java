package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.vo.post.LoadProductAndUserLikeVo;
import com.ftm.server.common.annotation.Port;
import java.util.List;
import java.util.Optional;

@Port
public interface LoadProductLikePort {
    List<LoadProductAndUserLikeVo> findProductLikeByUser(Long userId, List<Long> productIds);

    Optional<Long> findOneByUserAndProduct(Long userId, Long postProductId);
}
