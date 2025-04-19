package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindByPostProductIdsQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.PostProductImage;
import java.util.Map;

@Port
public interface LoadPostProductImagePort {

    Map<Long, PostProductImage> loadPostProductImagesByPostProductIds(
            FindByPostProductIdsQuery query);
}
