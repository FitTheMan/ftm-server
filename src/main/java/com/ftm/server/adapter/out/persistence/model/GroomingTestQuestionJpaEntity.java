package com.ftm.server.adapter.out.persistence.model;

import com.ftm.server.domain.entity.GroomingTestQuestion;
import com.ftm.server.domain.enums.GroomingCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "grooming_test_question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingTestQuestionJpaEntity extends BaseTimeJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, name = "grooming_category", columnDefinition = "grooming_category")
    private GroomingCategory groomingCategory;

    @Column(nullable = false)
    private String question;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingTestQuestionJpaEntity(GroomingCategory groomingCategory, String question) {
        this.groomingCategory = groomingCategory;
        this.question = question;
    }

    public static GroomingTestQuestionJpaEntity from(GroomingTestQuestion groomingTestQuestion) {
        return GroomingTestQuestionJpaEntity.builder()
                .groomingCategory(groomingTestQuestion.getGroomingCategory())
                .question(groomingTestQuestion.getQuestion())
                .build();
    }

    public void updateGroomingTestQuestionForDomainEntity(
            GroomingTestQuestion groomingTestQuestion) {
        this.groomingCategory = groomingTestQuestion.getGroomingCategory();
        this.question = groomingTestQuestion.getQuestion();
    }
}
