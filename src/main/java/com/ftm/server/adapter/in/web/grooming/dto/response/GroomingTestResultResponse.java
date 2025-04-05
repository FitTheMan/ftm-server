package com.ftm.server.adapter.in.web.grooming.dto.response;

import com.ftm.server.application.vo.grooming.GroomingLevelVo;
import com.ftm.server.application.vo.grooming.GroomingTestResultGradesVo;
import com.ftm.server.application.vo.grooming.GroomingTestResultScoresVo;
import com.ftm.server.application.vo.grooming.GroomingTestResultVo;
import lombok.Getter;

@Getter
public class GroomingTestResultResponse {

    private final GroomingTestResultScoresVo scores;
    private final GroomingTestResultGradesVo grades;
    private final GroomingLevelVo level;

    private GroomingTestResultResponse(GroomingTestResultVo result) {
        this.scores = result.getScores();
        this.grades = result.getGrades();
        this.level = result.getLevel();
    }

    public static GroomingTestResultResponse from(GroomingTestResultVo result) {
        return new GroomingTestResultResponse(result);
    }
}
