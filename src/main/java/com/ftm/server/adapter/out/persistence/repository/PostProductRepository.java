package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostProductRepository extends JpaRepository<PostProductJpaEntity, Long> {}
