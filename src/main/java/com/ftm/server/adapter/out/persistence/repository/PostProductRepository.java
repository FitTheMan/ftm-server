package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.adapter.out.persistence.model.PostProductJpaEntity;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostProductRepository extends JpaRepository<PostProductJpaEntity, Long> {

    List<PostProductJpaEntity> findAllByPost(PostJpaEntity post);

    List<PostProductJpaEntity> findAllByPostIdIn(List<Long> postIds);

    @Modifying
    @Query("DELETE FROM PostProductJpaEntity pp WHERE pp.id IN (:postProductIds)")
    void deleteAllByIdInBatch(@Param("postProductIds") List<Long> postProductIds);
}
