package com.ftm.server.application.service.grooming;

import static com.ftm.server.common.consts.StaticConsts.GROOMING_TESTS_INFO_CACHE_KEY_ALL;
import static com.ftm.server.common.consts.StaticConsts.GROOMING_TESTS_INFO_CACHE_NAME;

import com.ftm.server.application.port.in.grooming.LoadGroomingTestsUseCase;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingTestAnswerPort;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingTestQuestionPort;
import com.ftm.server.application.vo.grooming.GroomingTestAnswerInfoVo;
import com.ftm.server.application.vo.grooming.GroomingTestQuestionWithAnswersVo;
import com.ftm.server.domain.entity.GroomingTestAnswer;
import com.ftm.server.domain.entity.GroomingTestQuestion;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadGroomingTestsService implements LoadGroomingTestsUseCase {

    private final LoadGroomingTestQuestionPort loadGroomingTestQuestionPort;
    private final LoadGroomingTestAnswerPort loadGroomingTestAnswerPort;

    @Override
    @Cacheable(value = GROOMING_TESTS_INFO_CACHE_NAME, key = GROOMING_TESTS_INFO_CACHE_KEY_ALL)
    public Set<GroomingTestQuestionWithAnswersVo> execute() {
        // 모든 그루밍 테스트 질문 목록 조회
        List<GroomingTestQuestion> questions =
                loadGroomingTestQuestionPort.loadGroomingTestQuestions();

        // 모든 그루밍 테스트 답변 목록 조회
        List<GroomingTestAnswer> answers = loadGroomingTestAnswerPort.loadGroomingTestAnswers();

        // 같은 질문의 답변 목록끼리 그룹화
        Map<Long, List<GroomingTestAnswerInfoVo>> answersByQuestionId =
                answers.stream()
                        .map(GroomingTestAnswerInfoVo::from)
                        .collect(
                                Collectors.groupingBy(
                                        GroomingTestAnswerInfoVo::getGroomingTestQuestionId));

        // 각 그루밍 테스트 질문에 속한 답변목록을 함께 담아 응답, 순서 랜덤
        return questions.stream()
                .map(
                        question ->
                                GroomingTestQuestionWithAnswersVo.from(
                                        question,
                                        answersByQuestionId.getOrDefault(
                                                question.getId(), List.of())))
                .collect(Collectors.toSet());
    }
}
