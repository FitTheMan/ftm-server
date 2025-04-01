package com.ftm.server.domain.entity;

import com.ftm.server.domain.enums.GroomingCategory;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingTestQuestion extends BaseTime {

    private Long id;
    private GroomingCategory groomingCategory;
    private String question;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingTestQuestion(
            Long id,
            GroomingCategory groomingCategory,
            String question,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.groomingCategory = groomingCategory;
        this.question = question;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GroomingTestQuestion of(
            Long id,
            GroomingCategory groomingCategory,
            String question,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return GroomingTestQuestion.builder()
                .id(id)
                .groomingCategory(groomingCategory)
                .question(question)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
