package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.BeautyProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeautyProductRepository extends JpaRepository<BeautyProductJpaEntity, Long> {}
