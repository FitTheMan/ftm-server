package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.application.query.FindPostByDeleteOptionQuery;
import com.ftm.server.application.query.FindPostsByPagingQuery;
import java.util.List;
import org.springframework.data.domain.Slice;

public interface PostCustomRepository {

    List<PostJpaEntity> findAllByDeletedBefore(FindPostByDeleteOptionQuery query);

    Slice<PostJpaEntity> findAllByUserIdWithPaging(FindPostsByPagingQuery query);
}
