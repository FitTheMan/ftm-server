package com.ftm.server.domain.entity;

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
}
