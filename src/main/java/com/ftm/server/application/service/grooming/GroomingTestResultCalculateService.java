package com.ftm.server.application.service.grooming;

import static com.ftm.server.common.consts.StaticConsts.*;

import com.ftm.server.application.command.grooming.SubmitGroomingTestCommand;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingLevelPort;
import com.ftm.server.application.query.FIndGroomingLevelByScoreQuery;
import com.ftm.server.application.vo.grooming.*;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingLevel;
import com.ftm.server.domain.enums.GroomingCategory;
import com.ftm.server.domain.enums.GroomingCategoryGrade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroomingTestResultCalculateService {

    private final LoadGroomingLevelPort loadGroomingLevelPort;

    public GroomingTestResultVo process(SubmitGroomingTestCommand command) {
        // 그루밍 테스트 결과 점수 계산
        GroomingTestResultScoresVo scores = calculateScores(command.getSubmissions());

        // 그루밍 테스트 결과 등급 계산
        GroomingTestResultGradesVo grades = calculateGrades(scores);

        // 그루밍 레벨 조회
        GroomingLevel groomingLevel =
                loadGroomingLevelPort
                        .loadGroomingLevelByScore(
                                FIndGroomingLevelByScoreQuery.of(scores.getTotalScore()))
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorResponseCode.GROOMING_LEVEL_NOT_FOUND));
        GroomingLevelVo level = GroomingLevelVo.from(groomingLevel);

        return GroomingTestResultVo.from(command.getUserId(), scores, grades, level);
    }

    // 점수 계산
    private GroomingTestResultScoresVo calculateScores(
            List<SubmitGroomingTestCommand.SubmittedQuestion> submissions) {
        // 그루밍 카테고리별 점수 계산
        int beautyScore = 0;
        int hygieneScore = 0;
        int hairScore = 0;
        int workoutScore = 0;
        int fashionScore = 0;

        for (SubmitGroomingTestCommand.SubmittedQuestion result : submissions) {
            GroomingCategory category = GroomingCategory.from(result.getGroomingCategory());
            switch (category) {
                case BEAUTY -> beautyScore += sum(result);
                case HYGIENE -> hygieneScore += sum(result);
                case HAIR -> hairScore += sum(result);
                case WORKOUT -> workoutScore += sum(result);
                case FASHION -> fashionScore += sum(result);
            }
        }

        int totalScore = beautyScore + hygieneScore + hairScore + workoutScore + fashionScore;

        return GroomingTestResultScoresVo.of(
                beautyScore, hygieneScore, hairScore, workoutScore, fashionScore, totalScore);
    }

    private int sum(SubmitGroomingTestCommand.SubmittedQuestion submissions) {
        return submissions.getAnswers().stream()
                .mapToInt(SubmitGroomingTestCommand.SubmittedQuestion.SelectedAnswer::getScore)
                .sum();
    }

    // 등급 계산
    private GroomingTestResultGradesVo calculateGrades(GroomingTestResultScoresVo scores) {
        return GroomingTestResultGradesVo.from(
                GroomingCategoryGradeInfoVo.from(
                        GroomingCategoryGrade.fromScore(
                                scores.getBeautyScore(), BEAUTY_CATEGORY_MAX_SCORE)),
                GroomingCategoryGradeInfoVo.from(
                        GroomingCategoryGrade.fromScore(
                                scores.getHygieneScore(), HYGIENE_CATEGORY_MAX_SCORE)),
                GroomingCategoryGradeInfoVo.from(
                        GroomingCategoryGrade.fromScore(
                                scores.getHairScore(), HAIR_CATEGORY_MAX_SCORE)),
                GroomingCategoryGradeInfoVo.from(
                        GroomingCategoryGrade.fromScore(
                                scores.getWorkoutScore(), WORKOUT_CATEGORY_MAX_SCORE)),
                GroomingCategoryGradeInfoVo.from(
                        GroomingCategoryGrade.fromScore(
                                scores.getFashionScore(), FASHION_CATEGORY_MAX_SCORE)));
    }
}
