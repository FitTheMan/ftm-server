package com.ftm.server.adapter.out.persistence.model;

import com.ftm.server.domain.entity.GroomingTestAnswer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grooming_test_answer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingTestAnswerJpaEntity extends BaseTimeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grooming_test_question_id")
    private GroomingTestQuestionJpaEntity groomingTestQuestion;

    @Column(nullable = false)
    private String answer;

    private Integer score;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingTestAnswerJpaEntity(
            GroomingTestQuestionJpaEntity groomingTestQuestion, String answer, Integer score) {
        this.groomingTestQuestion = groomingTestQuestion;
        this.answer = answer;
        this.score = score;
    }

    public static GroomingTestAnswerJpaEntity from(
            GroomingTestAnswer groomingTestAnswer,
            GroomingTestQuestionJpaEntity groomingTestQuestion) {
        return GroomingTestAnswerJpaEntity.builder()
                .groomingTestQuestion(groomingTestQuestion)
                .answer(groomingTestAnswer.getAnswer())
                .score(groomingTestAnswer.getScore())
                .build();
    }

    public void updateGroomingTestAnswerForDomainEntity(
            GroomingTestQuestionJpaEntity groomingTestQuestionJpaEntity,
            GroomingTestAnswer groomingTestAnswer) {
        this.groomingTestQuestion = groomingTestQuestionJpaEntity;
        this.answer = groomingTestAnswer.getAnswer();
        this.score = groomingTestAnswer.getScore();
    }

    public void updateGroomingTestAnswerForDomainEntity(GroomingTestAnswer groomingTestAnswer) {
        this.answer = groomingTestAnswer.getAnswer();
        this.score = groomingTestAnswer.getScore();
    }
}
