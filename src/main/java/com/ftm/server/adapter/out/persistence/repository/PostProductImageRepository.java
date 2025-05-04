package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostProductImageJpaEntity;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostProductImageRepository extends JpaRepository<PostProductImageJpaEntity, Long> {

    List<PostProductImageJpaEntity> findAllByPostProductIdIn(List<Long> postProductIds);

    @Modifying
    @Query("DELETE FROM PostProductImageJpaEntity ppi WHERE ppi.id IN (:postProductImageIds)")
    void deleteAllByIdInBatch(@Param("postProductImageIds") List<Long> postProductImageIds);
}
