package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostProductImageJpaEntity;
import com.ftm.server.application.query.FindByIdsQuery;
import java.util.List;

public interface PostProductImageCustomRepository {

    List<PostProductImageJpaEntity> findByPostProductIds(FindByIdsQuery query);
}
