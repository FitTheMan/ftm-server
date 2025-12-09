package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostLikeJpaEntity;
import com.ftm.server.application.vo.post.LoadPostAndUserLikeVo;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeJpaEntity, Long> {

    @Query(
            """
        SELECT new com.ftm.server.application.vo.post.LoadPostAndUserLikeVo(
        p.id,
        :userId,
        (pl.id IS NOT NULL)
    )
    FROM PostJpaEntity p
    LEFT JOIN PostLikeJpaEntity pl
           ON pl.post.id = p.id AND pl.user.id = :userId
    WHERE p.id IN :postIds
    """)
    List<LoadPostAndUserLikeVo> findPostLikeByUser(
            @Param("userId") Long userId, @Param("postIds") List<Long> postIds);

    @Query(
            """
        SELECT new com.ftm.server.application.vo.post.LoadPostAndUserLikeVo(
        p.id,
        :userId,
        (pl.id IS NOT NULL)
    )
    FROM PostJpaEntity p
    LEFT JOIN PostLikeJpaEntity pl
           ON pl.post.id = p.id AND pl.user.id = :userId
    WHERE p.id = :postId
    """)
    LoadPostAndUserLikeVo findPostLikeByUser(
            @Param("userId") Long userId, @Param("postIds") Long postId);

    @Query("select p.id from PostLikeJpaEntity  p where p.user.id =:userId and p.post.id =:postId")
    Optional<Long> findByUserAndAndPost(@Param("userId") Long userId, @Param("postId") Long postId);
}
