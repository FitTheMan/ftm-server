package com.ftm.server.application.vo.grooming;

import lombok.Getter;

@Getter
public class GroomingTestResultScoresVo {

    private final int beautyScore;
    private final int hygieneScore;
    private final int hairScore;
    private final int workoutScore;
    private final int fashionScore;
    private final int totalScore;

    private GroomingTestResultScoresVo(
            int beautyScore,
            int hygieneScore,
            int hairScore,
            int workoutScore,
            int fashionScore,
            int totalScore) {
        this.beautyScore = beautyScore;
        this.hygieneScore = hygieneScore;
        this.hairScore = hairScore;
        this.workoutScore = workoutScore;
        this.fashionScore = fashionScore;
        this.totalScore = totalScore;
    }

    public static GroomingTestResultScoresVo of(
            int beautyScore,
            int hygieneScore,
            int hairScore,
            int workoutScore,
            int fashionScore,
            int totalScore) {
        return new GroomingTestResultScoresVo(
                beautyScore, hygieneScore, hairScore, workoutScore, fashionScore, totalScore);
    }
}
