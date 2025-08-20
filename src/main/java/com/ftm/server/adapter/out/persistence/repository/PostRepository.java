package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.application.vo.post.PostAndBookmarkCountVo;
import java.time.LocalDateTime;
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

    @Query("SELECT p FROM PostJpaEntity p WHERE p.user.id IN (:userIds)")
    List<PostJpaEntity> findAllByUserIdIn(@Param("userIds") List<Long> userIds);

    @Query(
            value =
                    """
        SELECT *
        FROM post p
        WHERE p.created_at >= :since
        ORDER BY
        (p.like_count / NULLIF(MAX(p.like_count) OVER (), 0.0)
        + p.view_count / NULLIF(MAX(p.view_count) OVER (), 0.0)) DESC ,
        p.id DESC
        LIMIT :limit
        """,
            nativeQuery = true)
    List<PostJpaEntity> findTopNPostsByViewCountAndLikeCount(
            @Param("since") LocalDateTime since, @Param("limit") int limit);

    @Query(
            """
        SELECT new com.ftm.server.application.vo.post.PostAndBookmarkCountVo(p.id, COUNT(b))
        FROM PostJpaEntity p
        LEFT JOIN BookmarkJpaEntity b ON b.post = p
        WHERE p.id IN :postIds
        GROUP BY p.id
    """)
    List<PostAndBookmarkCountVo> findBookmarkCountsByPostIds(@Param("postIds") List<Long> postIds);
}
