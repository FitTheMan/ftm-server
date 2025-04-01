package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.UserJpaEntity;
import com.ftm.server.domain.enums.SocialProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

    Boolean existsByEmail(String email);

    Optional<UserJpaEntity> findByEmail(String email);

    Optional<UserJpaEntity> findBySocialProviderAndSocialId(
            SocialProvider socialProvider, String socialId);

    Boolean existsBySocialIdAndSocialProvider(String socialId, SocialProvider socialProvider);
}
