package com.ftm.server.application.command.grooming.level;

import lombok.Getter;

@Getter
public class SaveGroomingLevelCommand {

    private final Integer minScore;
    private final Integer maxScore;
    private final String normalModeName;
    private final String normalModeSummary;
    private final String normalModeDescription;
    private final String truthModeName;
    private final String truthModeSummary;
    private final String truthModeDescription;
    private final String imagePath;

    private SaveGroomingLevelCommand(
            Integer minScore,
            Integer maxScore,
            String normalModeName,
            String normalModeSummary,
            String normalModeDescription,
            String truthModeName,
            String truthModeSummary,
            String truthModeDescription,
            String imagePath) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.normalModeName = normalModeName;
        this.normalModeSummary = normalModeSummary;
        this.normalModeDescription = normalModeDescription;
        this.truthModeName = truthModeName;
        this.truthModeSummary = truthModeSummary;
        this.truthModeDescription = truthModeDescription;
        this.imagePath = imagePath;
    }

    public static SaveGroomingLevelCommand of(
            Integer minScore,
            Integer maxScore,
            String normalModeName,
            String normalModeSummary,
            String normalModeDescription,
            String truthModeName,
            String truthModeSummary,
            String truthModeDescription,
            String imagePath) {
        return new SaveGroomingLevelCommand(
                minScore,
                maxScore,
                normalModeName,
                normalModeSummary,
                normalModeDescription,
                truthModeName,
                truthModeSummary,
                truthModeDescription,
                imagePath);
    }
}
