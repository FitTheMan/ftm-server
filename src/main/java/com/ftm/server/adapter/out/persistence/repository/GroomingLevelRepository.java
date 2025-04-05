package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.GroomingLevelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroomingLevelRepository
        extends JpaRepository<GroomingLevelJpaEntity, Long>, GroomingLevelCustomRepository {}
