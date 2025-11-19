package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.application.vo.post.ProductIdAndScoreVo;
import java.util.List;

public interface PostProductCustomRepository {
    List<ProductIdAndScoreVo> findAllByPopularity();
}
