package com.ftm.server.domain.entity;

import com.ftm.server.application.command.grooming.answer.SaveGroomingTestAnswerCommand;
import com.ftm.server.application.command.grooming.answer.UpdateGroomingTestAnswerCommand;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingTestAnswer extends BaseTime {

    private Long id;
    private Long groomingTestQuestionId;
    private String answer;
    private Integer score;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingTestAnswer(
            Long id,
            Long groomingTestQuestionId,
            String answer,
            Integer score,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.groomingTestQuestionId = groomingTestQuestionId;
        this.answer = answer;
        this.score = score;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GroomingTestAnswer of(
            Long id,
            Long groomingTestQuestionId,
            String answer,
            Integer score,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return GroomingTestAnswer.builder()
                .id(id)
                .groomingTestQuestionId(groomingTestQuestionId)
                .answer(answer)
                .score(score)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static GroomingTestAnswer create(SaveGroomingTestAnswerCommand command) {
        return GroomingTestAnswer.builder()
                .groomingTestQuestionId(command.getQuestionId())
                .answer(command.getAnswer())
                .score(command.getScore())
                .build();
    }

    public void update(UpdateGroomingTestAnswerCommand command) {
        if (command.getQuestionId() != null) this.groomingTestQuestionId = command.getQuestionId();
        if (command.getAnswer() != null) this.answer = command.getAnswer();
        if (command.getScore() != null) this.score = command.getScore();
    }
}
