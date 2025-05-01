package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.UserJpaEntity;
import com.ftm.server.domain.enums.SocialProvider;
import com.ftm.server.domain.enums.UserRole;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByIdAndIsDeleted(Long userId, Boolean isDeleted);

    Boolean existsByEmail(String email);

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findBySocialProviderAndSocialId(
            SocialProvider socialProvider, String socialId);

    Boolean existsBySocialIdAndSocialProvider(String socialId, SocialProvider socialProvider);

    Optional<UserJpaEntity> findByRole(UserRole role);

    @Modifying
    @Query("DELETE FROM UserJpaEntity u WHERE u.id in (:userIds)")
    void deleteAllByUserIdList(@Param("userIds") List<Long> userIds);

    @Query("SELECT u FROM UserJpaEntity u WHERE u.isDeleted = :isDeleted And u.deletedAt <=:end")
    List<UserJpaEntity> findAllByDeletedBefore(
            @Param("isDeleted") Boolean isDeleted, @Param("end") LocalDateTime end);
}
