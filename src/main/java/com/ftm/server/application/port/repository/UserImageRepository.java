package com.ftm.server.application.port.repository;

import com.ftm.server.domain.entity.UserImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    Optional<UserImage> findByUserId(Long userId);
}
