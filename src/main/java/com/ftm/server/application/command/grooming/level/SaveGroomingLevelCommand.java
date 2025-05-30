package com.ftm.server.application.command.grooming.level;

import lombok.Getter;

@Getter
public class SaveGroomingLevelCommand {

    private final Integer minScore;
    private final Integer maxScore;
    private final String mildLevelName;
    private final String spicyLevelName;

    private SaveGroomingLevelCommand(
            Integer minScore, Integer maxScore, String mildLevelName, String spicyLevelName) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.mildLevelName = mildLevelName;
        this.spicyLevelName = spicyLevelName;
    }

    public static SaveGroomingLevelCommand of(
            Integer minScore, Integer maxScore, String mildLevelName, String spicyLevelName) {
        return new SaveGroomingLevelCommand(minScore, maxScore, mildLevelName, spicyLevelName);
    }
}
