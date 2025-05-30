package com.ftm.server.domain.entity;

import com.ftm.server.application.command.grooming.question.SaveGroomingTestQuestionCommand;
import com.ftm.server.application.command.grooming.question.UpdateGroomingTestQuestionCommand;
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

    public static GroomingTestQuestion create(SaveGroomingTestQuestionCommand command) {
        return GroomingTestQuestion.builder()
                .groomingCategory(command.getGroomingCategory())
                .question(command.getQuestion())
                .build();
    }

    public void update(UpdateGroomingTestQuestionCommand command) {
        if (command.getGroomingCategory() != null)
            this.groomingCategory = command.getGroomingCategory();
        if (command.getQuestion() != null) this.question = command.getQuestion();
    }
}
