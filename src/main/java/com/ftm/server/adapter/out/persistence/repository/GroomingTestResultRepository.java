package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.GroomingTestResultJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroomingTestResultRepository
        extends JpaRepository<GroomingTestResultJpaEntity, Long>,
                GroomingTestResultCustomRepository {}
