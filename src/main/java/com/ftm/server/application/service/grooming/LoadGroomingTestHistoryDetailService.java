package com.ftm.server.application.service.grooming;

import static com.ftm.server.common.consts.StaticConsts.*;

import com.ftm.server.application.port.in.grooming.LoadGroomingTestHistoryDetailUseCase;
import com.ftm.server.application.port.out.cache.LoadGroomingTestsWithCachePort;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingLevelPort;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingTestResultPort;
import com.ftm.server.application.query.FIndGroomingLevelByScoreQuery;
import com.ftm.server.application.query.FindGroomingTestResultByUserIdAndTestedAtQuery;
import com.ftm.server.application.vo.grooming.*;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingLevel;
import com.ftm.server.domain.entity.GroomingTestResult;
import com.ftm.server.domain.enums.GroomingCategory;
import com.ftm.server.domain.enums.GroomingCategoryGrade;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadGroomingTestHistoryDetailService implements LoadGroomingTestHistoryDetailUseCase {

    private final LoadGroomingTestResultPort loadGroomingTestResultPort;
    private final LoadGroomingTestsWithCachePort loadGroomingTestsWithCachePort;
    private final LoadGroomingLevelPort loadGroomingLevelPort;

    @Override
    public GroomingTestHistoryDetailVo execute(
            FindGroomingTestResultByUserIdAndTestedAtQuery query) {
        // 특정 날짜의 그루밍 테스트 결과 목록 조회
        // 질문 ID → 선택한 답변 ID 목록으로 매핑
        List<GroomingTestResult> histories =
                loadGroomingTestResultPort.loadByUserIdAndTestedAt(query);
        Map<Long, List<Long>> userAnswerMap =
                histories.stream()
                        .collect(
                                Collectors.groupingBy(
                                        GroomingTestResult::getGroomingTestQuestionId,
                                        Collectors.mapping(
                                                GroomingTestResult::getGroomingTestAnswerId,
                                                Collectors.toList())));

        // 그루밍 테스트 계산 (점수, 등급, 레벨)
        GroomingTestResultScoresVo scores = calculateScores(userAnswerMap);
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

        return GroomingTestHistoryDetailVo.from(query.getTestedAt(), scores, grades, level);
    }

    // 점수 계산
    private GroomingTestResultScoresVo calculateScores(Map<Long, List<Long>> userAnswerMap) {
        // 캐싱된 그루밍 테스트 목록 조회
        List<GroomingTestQuestionWithAnswersVo> infos =
                loadGroomingTestsWithCachePort.loadGroomingTestsCache();
        Map<Long, GroomingTestQuestionWithAnswersVo> questionMap =
                infos.stream()
                        .collect(
                                Collectors.toMap(
                                        GroomingTestQuestionWithAnswersVo
                                                ::getGroomingTestQuestionId,
                                        Function.identity()));

        // 각 질문별 답변:점수 정보를 관리하는 map
        Map<Long, Map<Long, Integer>> scoreMap = toScoreMap(infos);

        // 그루밍 카테고리별 점수를 관리하는 map
        Map<GroomingCategory, Integer> categoryScores = new EnumMap<>(GroomingCategory.class);

        // 그루밍 카테고리별 점수 계산
        for (Map.Entry<Long, List<Long>> entry : userAnswerMap.entrySet()) {
            Long questionId = entry.getKey();

            GroomingCategory category = questionMap.get(questionId).getGroomingCategory();
            Map<Long, Integer> answersMap = scoreMap.get(questionId);

            int score =
                    entry.getValue().stream()
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
