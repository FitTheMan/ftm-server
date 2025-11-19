package com.ftm.server.application.port.out.cache;

import com.ftm.server.application.vo.post.ProductIdAndScoreVo;
import java.util.List;

public interface LoadProductsByPopularityCachePort {

    List<ProductIdAndScoreVo> loadProductsByPopularity();

    List<ProductIdAndScoreVo> updateProductsByPopularity();
}
