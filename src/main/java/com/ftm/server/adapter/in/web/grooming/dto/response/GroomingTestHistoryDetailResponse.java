package com.ftm.server.adapter.in.web.grooming.dto.response;

import com.ftm.server.application.vo.grooming.*;
import lombok.Getter;

@Getter
public class GroomingTestHistoryDetailResponse {

    private final String testedAt;
    private final GroomingTestResultScoresVo scores;
    private final GroomingTestResultGradesVo grades;
    private final GroomingLevelVo level;

    private GroomingTestHistoryDetailResponse(
            GroomingTestHistoryDetailVo groomingTestHistoryDetailVo) {
        this.testedAt = groomingTestHistoryDetailVo.getTestedAt();
        this.scores = groomingTestHistoryDetailVo.getScores();
        this.grades = groomingTestHistoryDetailVo.getGrades();
        this.level = groomingTestHistoryDetailVo.getLevel();
    }

    public static GroomingTestHistoryDetailResponse from(
            GroomingTestHistoryDetailVo groomingTestHistoryDetailVo) {
        return new GroomingTestHistoryDetailResponse(groomingTestHistoryDetailVo);
    }
}
