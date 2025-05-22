package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.GroomingTestAnswerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GroomingTestAnswerRepository
        extends JpaRepository<GroomingTestAnswerJpaEntity, Long> {

    @Modifying
    @Query(
            "DELETE FROM GroomingTestAnswerJpaEntity gta WHERE gta.groomingTestQuestion.id = (:questionId)")
    void deleteAllByGroomingTestQuestionId(Long questionId);
}
