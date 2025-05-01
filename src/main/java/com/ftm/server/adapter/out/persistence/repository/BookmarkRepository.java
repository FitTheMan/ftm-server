package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.BookmarkJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<BookmarkJpaEntity, Long> {
    @Modifying
    @Query("DELETE FROM BookmarkJpaEntity b WHERE b.user.id in (:userIds)")
    void deleteAllByUserIdList(@Param("userIds") List<Long> userIds);
}
