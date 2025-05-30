package com.ftm.server.application.command.grooming.level;

import lombok.Getter;

@Getter
public class UpdateGroomingLevelCommand {

    private final Long id;
    private final Integer minScore;
    private final Integer maxScore;
    private final String mildLevelName;
    private final String spicyLevelName;

    private UpdateGroomingLevelCommand(
            Long id,
            Integer minScore,
            Integer maxScore,
            String mildLevelName,
            String spicyLevelName) {
        this.id = id;
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.mildLevelName = mildLevelName;
        this.spicyLevelName = spicyLevelName;
    }

    public static UpdateGroomingLevelCommand of(
            Long id,
            Integer minScore,
            Integer maxScore,
            String mildLevelName,
            String spicyLevelName) {
        return new UpdateGroomingLevelCommand(
                id, minScore, maxScore, mildLevelName, spicyLevelName);
    }
}
