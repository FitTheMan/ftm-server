package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.UserImageJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserImageRepository extends JpaRepository<UserImageJpaEntity, Long> {

    Optional<UserImageJpaEntity> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM UserImageJpaEntity u WHERE u.user.id in (:userIds)")
    void deleteAllByUserIdList(@Param("userIds") List<Long> userIds);

    @Query("SELECT ui.objectKey FROM UserImageJpaEntity ui WHERE ui.user.id in (:userIds)")
    List<String> findAllByUserIdList(@Param("userIds") List<Long> userIds);
}
