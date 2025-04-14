package com.ftm.server.application.vo.grooming;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class GroomingTestHistoryDetailVo {

    private final String testedAt;
    private final GroomingTestResultScoresVo scores;
    private final GroomingTestResultGradesVo grades;
    private final GroomingLevelVo level;

    private GroomingTestHistoryDetailVo(
            String testedAt,
            GroomingTestResultScoresVo scores,
            GroomingTestResultGradesVo grades,
            GroomingLevelVo level) {
        this.testedAt = testedAt;
        this.scores = scores;
        this.grades = grades;
        this.level = level;
    }

    public static GroomingTestHistoryDetailVo from(
            LocalDate testedAt,
            GroomingTestResultScoresVo scores,
            GroomingTestResultGradesVo grades,
            GroomingLevelVo level) {
        return new GroomingTestHistoryDetailVo(
                testedAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), scores, grades, level);
    }
}
