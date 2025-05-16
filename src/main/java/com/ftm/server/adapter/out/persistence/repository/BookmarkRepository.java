package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.BookmarkJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository
        extends JpaRepository<BookmarkJpaEntity, Long>, BookmarkCustomRepository {
    @Modifying
    @Query("DELETE FROM BookmarkJpaEntity b WHERE b.user.id in (:userIds)")
    void deleteAllByUserIdList(@Param("userIds") List<Long> userIds);

    @Modifying
    @Query(
            value =
                    "INSERT INTO bookmark (user_id, post_id) "
                            + "VALUES (:userId, :postId) "
                            + "ON CONFLICT (user_id, post_id) "
                            + "DO NOTHING;",
            nativeQuery = true)
    int saveOrUpdate(@Param("userId") Long userId, @Param("postId") Long postId);

    Boolean existsByUserIdAndPostId(Long userId, Long postId);

    @Query(
            value =
                    "select b from BookmarkJpaEntity b where b.user.id =:userId and b.post.id =:postId")
    Optional<BookmarkJpaEntity> findByUserIdAndPostId(
            @Param(value = "userId") Long userId, @Param(value = "postId") Long postId);
}
