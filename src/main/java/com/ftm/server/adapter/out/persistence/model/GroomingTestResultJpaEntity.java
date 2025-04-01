package com.ftm.server.adapter.out.persistence.model;

import jakarta.persistence.*;
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

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingTestResultJpaEntity(
            UserJpaEntity user,
            GroomingTestQuestionJpaEntity groomingTestQuestion,
            GroomingTestAnswerJpaEntity groomingTestAnswer) {
        this.user = user;
        this.groomingTestQuestion = groomingTestQuestion;
        this.groomingTestAnswer = groomingTestAnswer;
    }

    public static GroomingTestResultJpaEntity from(
            UserJpaEntity userJpaEntity,
            GroomingTestQuestionJpaEntity groomingTestQuestionJpaEntity,
            GroomingTestAnswerJpaEntity groomingTestAnswerJpaEntity) {
        return GroomingTestResultJpaEntity.builder()
                .user(userJpaEntity)
                .groomingTestQuestion(groomingTestQuestionJpaEntity)
                .groomingTestAnswer(groomingTestAnswerJpaEntity)
                .build();
    }
}
