package com.ftm.server.application.service.grooming;

import com.ftm.server.application.port.out.cache.LoadGroomingTestsWithCachePort;
import com.ftm.server.application.vo.grooming.GroomingTestAnswerInfoVo;
import com.ftm.server.application.vo.grooming.GroomingTestQuestionWithAnswersVo;
import com.ftm.server.application.vo.grooming.SubmitGroomingTestVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroomingTestValidator {

    private final LoadGroomingTestsWithCachePort loadGroomingTestsWithCachePort;

    public void execute(List<SubmitGroomingTestVo> submissions) {
        // 1. 질문 ID 중복 검사
        List<Long> questionIds =
                submissions.stream().map(SubmitGroomingTestVo::getQuestionId).toList();
        if (questionIds.size() != new HashSet<>(questionIds).size()) {
            throw new CustomException(ErrorResponseCode.INVALID_GROOMING_TEST_QUESTION_ID);
        }

        // 2. 캐시에서 유효한 질문/답변 조회
        List<GroomingTestQuestionWithAnswersVo> infos =
                loadGroomingTestsWithCachePort.loadGroomingTestsCache();

        Map<Long, Set<Long>> validAnswerMap =
                infos.stream()
                        .collect(
                                Collectors.toMap(
                                        GroomingTestQuestionWithAnswersVo
                                                ::getGroomingTestQuestionId,
                                        vo ->
                                                vo.getAnswers().stream()
                                                        .map(
                                                                GroomingTestAnswerInfoVo
                                                                        ::getGroomingTestAnswerId)
                                                        .collect(Collectors.toSet())));

        // 3. 유효성 검사
        for (SubmitGroomingTestVo submitted : submissions) {
            Long questionId = submitted.getQuestionId();
            List<Long> answerIds = submitted.getAnswerIds();

            // 유효하지 않은 질문이 존재할 경우
            if (!validAnswerMap.containsKey(questionId)) {
                throw new CustomException(ErrorResponseCode.INVALID_GROOMING_TEST_QUESTION_ID);
            }

            Set<Long> validAnswerIds = validAnswerMap.get(questionId); // 해당 질문에 속한 답변 목록
            for (Long answerId : answerIds) {
                // 유효하지 않은 답변이 존재할 경우, 각 질문에 속한 답변이 아닐 경우
                if (!validAnswerIds.contains(answerId)) {
                    throw new CustomException(ErrorResponseCode.INVALID_GROOMING_TEST_ANSWER_ID);
                }
            }

            // 4. 답변 ID 중복 검사
            if (answerIds.size() != new HashSet<>(answerIds).size()) {
                throw new CustomException(ErrorResponseCode.INVALID_GROOMING_TEST_ANSWER_ID);
            }
        }
    }
}
