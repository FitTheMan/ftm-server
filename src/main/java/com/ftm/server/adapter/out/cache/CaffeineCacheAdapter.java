package com.ftm.server.adapter.out.cache;

import static com.ftm.server.common.consts.StaticConsts.GROOMING_TESTS_INFO_CACHE_KEY_ALL;
import static com.ftm.server.common.consts.StaticConsts.GROOMING_TESTS_INFO_CACHE_NAME;

import com.ftm.server.adapter.out.persistence.mapper.GroomingTestAnswerMapper;
import com.ftm.server.adapter.out.persistence.mapper.GroomingTestQuestionMapper;
import com.ftm.server.adapter.out.persistence.repository.GroomingTestAnswerRepository;
import com.ftm.server.adapter.out.persistence.repository.GroomingTestQuestionRepository;
import com.ftm.server.application.port.out.cache.LoadGroomingTestsWithCachePort;
import com.ftm.server.application.vo.grooming.GroomingTestAnswerInfoVo;
import com.ftm.server.application.vo.grooming.GroomingTestQuestionWithAnswersVo;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.domain.entity.GroomingTestAnswer;
import com.ftm.server.domain.entity.GroomingTestQuestion;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class CaffeineCacheAdapter implements LoadGroomingTestsWithCachePort {

    private final GroomingTestQuestionRepository groomingTestQuestionRepository;
    private final GroomingTestAnswerRepository groomingTestAnswerRepository;

    private final GroomingTestQuestionMapper groomingTestQuestionMapper;
    private final GroomingTestAnswerMapper groomingTestAnswerMapper;

    @Cacheable(value = GROOMING_TESTS_INFO_CACHE_NAME, key = GROOMING_TESTS_INFO_CACHE_KEY_ALL)
    @Override
    public List<GroomingTestQuestionWithAnswersVo> loadGroomingTestsCache() {
        // 모든 그루밍 테스트 질문 목록 조회
        List<GroomingTestQuestion> questions =
                groomingTestQuestionRepository.findAll().stream()
                        .map(groomingTestQuestionMapper::toDomain)
                        .toList();

        // 모든 그루밍 테스트 답변 목록 조회
        List<GroomingTestAnswer> answers =
                groomingTestAnswerRepository.findAll().stream()
                        .map(groomingTestAnswerMapper::toDomain)
                        .toList();

        // 같은 질문의 답변 목록끼리 그룹화
        Map<Long, List<GroomingTestAnswerInfoVo>> answersByQuestionId =
                answers.stream()
                        .collect(
                                Collectors.groupingBy(
                                        GroomingTestAnswer
                                                ::getGroomingTestQuestionId, // 질문 ID를 기준으로 답변 그룹화
                                        Collectors.mapping(
                                                GroomingTestAnswerInfoVo::from,
                                                Collectors.toList())));

        // 각 그루밍 테스트 질문에 속한 답변목록을 함께 담아 응답
        return questions.stream()
                .map(
                        question ->
                                GroomingTestQuestionWithAnswersVo.from(
                                        question,
                                        answersByQuestionId.getOrDefault(
                                                question.getId(), List.of())))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
