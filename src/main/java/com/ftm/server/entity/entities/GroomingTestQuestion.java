package com.ftm.server.entity.entities;

import com.ftm.server.entity.enums.GroomingCategory;
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
public class GroomingTestQuestion extends BaseEntity {
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
    private GroomingTestQuestion(GroomingCategory groomingCategory, String question) {
        this.groomingCategory = groomingCategory;
        this.question = question;
    }
}
