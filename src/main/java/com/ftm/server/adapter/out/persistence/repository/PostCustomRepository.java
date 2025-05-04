package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.application.query.FindPostByDeleteOptionQuery;
import java.util.List;

public interface PostCustomRepository {

    List<PostJpaEntity> findAllByDeletedBefore(FindPostByDeleteOptionQuery query);
}
