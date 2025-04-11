package com.ftm.server.application.service.grooming;

import static com.ftm.server.common.consts.StaticConsts.*;

import com.ftm.server.application.command.grooming.SubmitGroomingTestCommand;
import com.ftm.server.application.port.in.grooming.SubmitGroomingTestUseCase;
import com.ftm.server.application.port.out.cache.LoadGroomingTestsWithCachePort;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingLevelPort;
import com.ftm.server.application.query.FIndGroomingLevelByScoreQuery;
import com.ftm.server.application.vo.grooming.*;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingLevel;
import com.ftm.server.domain.enums.GroomingCategory;
import com.ftm.server.domain.enums.GroomingCategoryGrade;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmitGroomingTestService implements SubmitGroomingTestUseCase {

    private final LoadGroomingLevelPort loadGroomingLevelPort;
    private final LoadGroomingTestsWithCachePort loadGroomingTestsWithCachePort;

    private final GroomingTestValidator groomingTestValidator;

    @Override
    @Transactional(readOnly = true)
    public GroomingTestResultVo execute(SubmitGroomingTestCommand command) {
        // 그루밍 테스트 유효성 검증
        List<SubmitGroomingTestVo> submissions = SubmitGroomingTestVo.from(command);
        groomingTestValidator.execute(submissions);

        // 그루밍 테스트 계산 (점수, 등급, 레벨)
        GroomingTestResultScoresVo scores = calculateScores(command);
        GroomingTestResultGradesVo grades = calculateGrades(scores);
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
    private GroomingTestResultScoresVo calculateScores(SubmitGroomingTestCommand command) {
        // 캐싱된 그루밍 테스트 목록 조회
        List<GroomingTestQuestionWithAnswersVo> infos =
                loadGroomingTestsWithCachePort.loadGroomingTestsCache();

        // 각 질문별 답변:점수 정보를 관리하는 map
        Map<Long, Map<Long, Integer>> scoreMap = toScoreMap(infos);

        // 그루밍 카테고리별 점수를 관리하는 map
        Map<GroomingCategory, Integer> categoryScores = new EnumMap<>(GroomingCategory.class);

        // 그루밍 카테고리별 점수 계산
        for (SubmitGroomingTestCommand.SubmittedQuestion sub : command.getSubmissions()) {
            GroomingCategory category = GroomingCategory.from(sub.getGroomingCategory());
            Map<Long, Integer> answersMap = scoreMap.get(sub.getQuestionId());

            int score =
                    sub.getAnswerIds().stream()
                            .mapToInt(ans -> answersMap.getOrDefault(ans, 0))
                            .sum();

            categoryScores.merge(category, score, Integer::sum);
        }

        return GroomingTestResultScoresVo.of(
                categoryScores.getOrDefault(GroomingCategory.BEAUTY, 0),
                categoryScores.getOrDefault(GroomingCategory.HYGIENE, 0),
                categoryScores.getOrDefault(GroomingCategory.HAIR, 0),
                categoryScores.getOrDefault(GroomingCategory.WORKOUT, 0),
                categoryScores.getOrDefault(GroomingCategory.FASHION, 0),
                categoryScores.values().stream().mapToInt(Integer::intValue).sum());
    }

    private Map<Long, Map<Long, Integer>> toScoreMap(
            List<GroomingTestQuestionWithAnswersVo> infos) {
        return infos.stream()
                .collect(
                        Collectors.toMap(
                                GroomingTestQuestionWithAnswersVo::getGroomingTestQuestionId,
                                question ->
                                        question.getAnswers().stream()
                                                .collect(
                                                        Collectors.toMap(
                                                                GroomingTestAnswerInfoVo
                                                                        ::getGroomingTestAnswerId,
                                                                GroomingTestAnswerInfoVo
                                                                        ::getScore))));
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
