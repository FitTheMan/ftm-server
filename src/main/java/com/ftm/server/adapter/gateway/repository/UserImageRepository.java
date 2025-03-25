package com.ftm.server.adapter.gateway.repository;

import com.ftm.server.entity.entities.UserImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    Optional<UserImage> findByUserId(Long userId);
}
