package com.ftm.server.adapter.out.persistence.model;

import com.ftm.server.domain.entity.GroomingTestResult;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grooming_test_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingTestResultJpaEntity extends BaseTimeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grooming_test_question_id")
    private GroomingTestQuestionJpaEntity groomingTestQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grooming_test_answer_id")
    private GroomingTestAnswerJpaEntity groomingTestAnswer;

    private LocalDateTime testedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingTestResultJpaEntity(
            UserJpaEntity user,
            GroomingTestQuestionJpaEntity groomingTestQuestion,
            GroomingTestAnswerJpaEntity groomingTestAnswer,
            LocalDateTime testedAt) {
        this.user = user;
        this.groomingTestQuestion = groomingTestQuestion;
        this.groomingTestAnswer = groomingTestAnswer;
        this.testedAt = testedAt;
    }

    public static GroomingTestResultJpaEntity from(
            UserJpaEntity userJpaEntity,
            GroomingTestQuestionJpaEntity groomingTestQuestionJpaEntity,
            GroomingTestAnswerJpaEntity groomingTestAnswerJpaEntity,
            GroomingTestResult groomingTestResult) {
        return GroomingTestResultJpaEntity.builder()
                .user(userJpaEntity)
                .groomingTestQuestion(groomingTestQuestionJpaEntity)
                .groomingTestAnswer(groomingTestAnswerJpaEntity)
                .testedAt(groomingTestResult.getTestedAt())
                .build();
    }
}
