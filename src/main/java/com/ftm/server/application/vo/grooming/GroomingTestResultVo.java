package com.ftm.server.application.vo.grooming;

import lombok.Getter;

@Getter
public class GroomingTestResultVo {

    private final boolean isAuthenticated;
    private final GroomingTestResultScoresVo scores;
    private final GroomingTestResultGradesVo grades;
    private final GroomingLevelVo level;

    private GroomingTestResultVo(
            boolean isAuthenticated,
            GroomingTestResultScoresVo scores,
            GroomingTestResultGradesVo grades,
            GroomingLevelVo level) {
        this.isAuthenticated = isAuthenticated;
        this.scores = scores;
        this.grades = grades;
        this.level = level;
    }

    public static GroomingTestResultVo from(
            Long userId,
            GroomingTestResultScoresVo scores,
            GroomingTestResultGradesVo grades,
            GroomingLevelVo level) {
        boolean isAuthenticated = userId != null;
        return new GroomingTestResultVo(isAuthenticated, scores, grades, level);
    }
}
