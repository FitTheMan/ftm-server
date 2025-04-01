package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostProductImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostProductImageRepository
        extends JpaRepository<PostProductImageJpaEntity, Long> {}
