package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository
        extends JpaRepository<PostJpaEntity, Long>,
                PostCustomRepository,
                PostWithBookmarkCustomRepository {

    List<PostJpaEntity> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM PostJpaEntity p WHERE p.id IN (:postIds)")
    void deleteAllByIdInBatch(@Param("postIds") List<Long> postIds);

    boolean existsById(Long id);
}
