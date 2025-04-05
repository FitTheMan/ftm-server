package com.ftm.server.domain.entity;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingTestResult extends BaseTime {

    private Long id;
    private Long userId;
    private Long groomingTestQuestionId;
    private Long groomingTestAnswerId;
    private LocalDateTime testedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingTestResult(
            Long id,
            Long userId,
            Long groomingTestAnswerId,
            Long groomingTestQuestionId,
            LocalDateTime testedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.groomingTestQuestionId = groomingTestQuestionId;
        this.groomingTestAnswerId = groomingTestAnswerId;
        this.testedAt = testedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GroomingTestResult of(
            Long id,
            Long userId,
            Long groomingTestQuestionId,
            Long groomingTestAnswerId,
            LocalDateTime testedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return GroomingTestResult.builder()
                .id(id)
                .userId(userId)
                .groomingTestQuestionId(groomingTestQuestionId)
                .groomingTestAnswerId(groomingTestAnswerId)
                .testedAt(testedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static GroomingTestResult create(
            Long userId,
            Long groomingTestQuestionId,
            Long groomingTestAnswerId,
            LocalDateTime testedAt) {
        return GroomingTestResult.builder()
                .userId(userId)
                .groomingTestQuestionId(groomingTestQuestionId)
                .groomingTestAnswerId(groomingTestAnswerId)
                .testedAt(testedAt)
                .build();
    }
}
