package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.GroomingTestQuestionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroomingTestQuestionRepository
        extends JpaRepository<GroomingTestQuestionJpaEntity, Long> {}
