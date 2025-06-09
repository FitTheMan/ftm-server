package com.ftm.server.application.query;

import lombok.Getter;

@Getter
public class FindGroomingLevelByScoreQuery {

    private final int score;

    private FindGroomingLevelByScoreQuery(int score) {
        this.score = score;
    }

    public static FindGroomingLevelByScoreQuery of(int score) {
        return new FindGroomingLevelByScoreQuery(score);
    }
}
