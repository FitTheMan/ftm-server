package com.ftm.server.application.command.grooming.level;

import lombok.Getter;

@Getter
public class UpdateGroomingLevelCommand {

    private final Long id;
    private final Integer minScore;
    private final Integer maxScore;
    private final String normalModeName;
    private final String normalModeSummary;
    private final String normalModeDescription;
    private final String truthModeName;
    private final String truthModeSummary;
    private final String truthModeDescription;

    private UpdateGroomingLevelCommand(
            Long id,
            Integer minScore,
            Integer maxScore,
            String normalModeName,
            String normalModeSummary,
            String normalModeDescription,
            String truthModeName,
            String truthModeSummary,
            String truthModeDescription) {
        this.id = id;
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.normalModeName = normalModeName;
        this.normalModeSummary = normalModeSummary;
        this.normalModeDescription = normalModeDescription;
        this.truthModeName = truthModeName;
        this.truthModeSummary = truthModeSummary;
        this.truthModeDescription = truthModeDescription;
    }

    public static UpdateGroomingLevelCommand of(
            Long id,
            Integer minScore,
            Integer maxScore,
            String normalModeName,
            String normalModeSummary,
            String normalModeDescription,
            String truthModeName,
            String truthModeSummary,
            String truthModeDescription) {
        return new UpdateGroomingLevelCommand(
                id,
                minScore,
                maxScore,
                normalModeName,
                normalModeSummary,
                normalModeDescription,
                truthModeName,
                truthModeSummary,
                truthModeDescription);
    }
}
