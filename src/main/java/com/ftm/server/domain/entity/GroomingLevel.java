package com.ftm.server.domain.entity;

import com.ftm.server.application.command.grooming.level.SaveGroomingLevelCommand;
import com.ftm.server.application.command.grooming.level.UpdateGroomingLevelCommand;
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
    private String normalModeName;
    private String normalModeSummary;
    private String normalModeDescription;
    private String truthModeName;
    private String truthModeSummary;
    private String truthModeDescription;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingLevel(
            Long id,
            Integer minScore,
            Integer maxScore,
            String normalModeName,
            String normalModeSummary,
            String normalModeDescription,
            String truthModeName,
            String truthModeSummary,
            String truthModeDescription,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.normalModeName = normalModeName;
        this.normalModeSummary = normalModeSummary;
        this.normalModeDescription = normalModeDescription;
        this.truthModeName = truthModeName;
        this.truthModeSummary = truthModeSummary;
        this.truthModeDescription = truthModeDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GroomingLevel of(
            Long id,
            Integer minScore,
            Integer maxScore,
            String normalModeName,
            String normalModeSummary,
            String normalModeDescription,
            String truthModeName,
            String truthModeSummary,
            String truthModeDescription,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return GroomingLevel.builder()
                .id(id)
                .minScore(minScore)
                .maxScore(maxScore)
                .normalModeName(normalModeName)
                .normalModeSummary(normalModeSummary)
                .normalModeDescription(normalModeDescription)
                .truthModeName(truthModeName)
                .truthModeSummary(truthModeSummary)
                .truthModeDescription(truthModeDescription)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static GroomingLevel create(SaveGroomingLevelCommand command) {
        return GroomingLevel.builder()
                .minScore(command.getMinScore())
                .maxScore(command.getMaxScore())
                .normalModeName(command.getNormalModeName())
                .normalModeSummary(command.getNormalModeSummary())
                .normalModeDescription(command.getNormalModeDescription())
                .truthModeName(command.getTruthModeName())
                .truthModeSummary(command.getTruthModeSummary())
                .truthModeDescription(command.getTruthModeDescription())
                .build();
    }

    public void update(UpdateGroomingLevelCommand command) {
        if (command.getMinScore() != null) this.minScore = command.getMinScore();
        if (command.getMaxScore() != null) this.maxScore = command.getMaxScore();
        if (command.getNormalModeName() != null) this.normalModeName = command.getNormalModeName();
        if (command.getNormalModeSummary() != null)
            this.normalModeSummary = command.getNormalModeSummary();
        if (command.getNormalModeDescription() != null)
            this.normalModeDescription = command.getNormalModeDescription();
        if (command.getTruthModeName() != null) this.truthModeName = command.getTruthModeName();
        if (command.getTruthModeSummary() != null)
            this.truthModeSummary = command.getTruthModeSummary();
        if (command.getTruthModeDescription() != null)
            this.truthModeDescription = command.getTruthModeDescription();
    }
}
