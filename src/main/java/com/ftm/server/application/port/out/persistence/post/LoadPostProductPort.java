package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindByPostIdQuery;
import com.ftm.server.application.query.FindByProductHashTagsQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.PostProduct;
import java.util.List;

@Port
public interface LoadPostProductPort {

    List<PostProduct> loadPostProductsByPostId(FindByPostIdQuery query);

    List<PostProduct> loadPostProductsByIds(FindByIdsQuery query);

    List<PostProduct> loadPostProductsByPostIds(FindByIdsQuery query);

    List<PostProduct> loadPostProductsByHashTags(FindByProductHashTagsQuery query);

    List<PostProduct> loadAllPostProduct();
}
