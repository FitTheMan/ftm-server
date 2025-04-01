package com.ftm.server.domain.entity;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingLevel extends BaseTime {

    private Long id;
    private Integer minScore;
    private Integer maxScore;
    private String mildLevelName;
    private String spicyLevelName;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingLevel(
            Long id,
            Integer minScore,
            Integer maxScore,
            String mildLevelName,
            String spicyLevelName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.mildLevelName = mildLevelName;
        this.spicyLevelName = spicyLevelName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GroomingLevel of(
            Long id,
            Integer minScore,
            Integer maxScore,
            String mildLevelName,
            String spicyLevelName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return GroomingLevel.builder()
                .id(id)
                .minScore(minScore)
                .maxScore(maxScore)
                .mildLevelName(mildLevelName)
                .spicyLevelName(spicyLevelName)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
