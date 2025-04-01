package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.UserImageJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImageJpaEntity, Long> {

    Optional<UserImageJpaEntity> findByUserId(Long userId);
}
