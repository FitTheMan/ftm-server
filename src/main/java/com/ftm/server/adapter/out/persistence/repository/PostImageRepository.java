package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostImageJpaEntity;
import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostImageRepository extends JpaRepository<PostImageJpaEntity, Long> {

    List<PostImageJpaEntity> findAllByPost(PostJpaEntity post);

    List<PostImageJpaEntity> findAllByPostIdIn(List<Long> postIds);

    @Modifying
    @Query("DELETE FROM PostImageJpaEntity pi WHERE pi.id IN (:postImageIds)")
    void deleteAllByIdInBatch(@Param("postImageIds") List<Long> postImageIds);
}
