package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.GroomingTestAnswerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroomingTestAnswerRepository
        extends JpaRepository<GroomingTestAnswerJpaEntity, Long> {}
