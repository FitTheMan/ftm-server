package com.ftm.server.entity.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grooming_test_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingTestResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grooming_test_answer_id")
    private GroomingTestAnswer groomingTestAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grooming_test_question_id")
    private GroomingTestQuestion groomingTestQuestion;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingTestResult(
            User user,
            GroomingTestAnswer groomingTestAnswer,
            GroomingTestQuestion groomingTestQuestion) {
        this.user = user;
        this.groomingTestAnswer = groomingTestAnswer;
        this.groomingTestQuestion = groomingTestQuestion;
    }
}
