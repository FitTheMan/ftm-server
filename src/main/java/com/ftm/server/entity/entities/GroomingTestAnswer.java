package com.ftm.server.entity.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grooming_test_answer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingTestAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grooming_test_question_id")
    private GroomingTestQuestion groomingTestQuestion;

    @Column(nullable = false)
    private String answer;

    private Integer score;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingTestAnswer(
            GroomingTestQuestion groomingTestQuestion, String answer, Integer score) {
        this.groomingTestQuestion = groomingTestQuestion;
        this.answer = answer;
        this.score = score;
    }
}
