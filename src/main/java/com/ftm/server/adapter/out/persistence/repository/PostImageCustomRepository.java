package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostImageJpaEntity;
import com.ftm.server.application.query.FindByIdsQuery;
import java.util.List;

public interface PostImageCustomRepository {

    // 여러개의 게시글 이미지 중 대표 이미지 한 개 조회 (썸네일용 이미지, 업로드가 가장 먼저된 이미지 조회)
    List<PostImageJpaEntity> findRepresentativeImagesByPostIdIn(FindByIdsQuery query);
}
