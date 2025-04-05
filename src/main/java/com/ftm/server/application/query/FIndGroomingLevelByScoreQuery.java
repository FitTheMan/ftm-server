package com.ftm.server.application.query;

import lombok.Getter;

@Getter
public class FIndGroomingLevelByScoreQuery {

    private final int score;

    private FIndGroomingLevelByScoreQuery(int score) {
        this.score = score;
    }

    public static FIndGroomingLevelByScoreQuery of(int score) {
        return new FIndGroomingLevelByScoreQuery(score);
    }
}
