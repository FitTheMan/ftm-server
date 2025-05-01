package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.GroomingTestResultJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroomingTestResultRepository
        extends JpaRepository<GroomingTestResultJpaEntity, Long>,
                GroomingTestResultCustomRepository {
    @Modifying
    @Query("DELETE FROM GroomingTestResultJpaEntity g WHERE g.user.id in (:userIds)")
    void deleteAllByUserIdList(@Param("userIds") List<Long> userIds);
}
