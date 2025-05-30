package com.ftm.server.grooming.answer;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.grooming.dto.request.UpdateGroomingTestAnswerRequest;
import com.ftm.server.adapter.out.persistence.mapper.GroomingTestAnswerMapper;
import com.ftm.server.adapter.out.persistence.mapper.GroomingTestQuestionMapper;
import com.ftm.server.adapter.out.persistence.model.GroomingTestQuestionJpaEntity;
import com.ftm.server.adapter.out.persistence.repository.GroomingTestAnswerRepository;
import com.ftm.server.adapter.out.persistence.repository.GroomingTestQuestionRepository;
import com.ftm.server.application.command.grooming.answer.SaveGroomingTestAnswerCommand;
import com.ftm.server.application.command.grooming.question.SaveGroomingTestQuestionCommand;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingTestAnswer;
import com.ftm.server.domain.entity.GroomingTestQuestion;
import com.ftm.server.domain.enums.GroomingCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class UpdateGroomingTestAnswerTest extends BaseTest {

    @Autowired private GroomingTestQuestionRepository groomingTestQuestionRepository;
    @Autowired private GroomingTestQuestionMapper groomingTestQuestionMapper;
    @Autowired private GroomingTestAnswerRepository groomingTestAnswerRepository;
    @Autowired private GroomingTestAnswerMapper groomingTestAnswerMapper;

    private Long savedAnswerId;

    private ResultActions getResultActions(
            Long answerId, UpdateGroomingTestAnswerRequest request, MockHttpSession session)
            throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.patch(
                                "/api/grooming/tests/answers/{answerId}", answerId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request))
                        .session(session));
    }

    @BeforeEach
    void setUp() {
        SaveGroomingTestQuestionCommand questionCommand =
                SaveGroomingTestQuestionCommand.of(GroomingCategory.BEAUTY, "그루밍 테스트 질문");
        GroomingTestQuestion question = GroomingTestQuestion.create(questionCommand);
        GroomingTestQuestionJpaEntity questionJpaEntity =
                groomingTestQuestionRepository.save(
                        groomingTestQuestionMapper.toJpaEntity(question));

        SaveGroomingTestAnswerCommand answerCommand =
                SaveGroomingTestAnswerCommand.of(questionJpaEntity.getId(), "그루밍 테스트 답변", 1);
        GroomingTestAnswer answer = GroomingTestAnswer.create(answerCommand);
        savedAnswerId =
                groomingTestAnswerRepository
                        .save(groomingTestAnswerMapper.toJpaEntity(answer, questionJpaEntity))
                        .getId();
    }

    @Test
    @Transactional
    void 그루밍_테스트_답변_수정_성공() throws Exception {
        // given
        UpdateGroomingTestAnswerRequest request =
                new UpdateGroomingTestAnswerRequest(null, "그루밍 테스트 답변 수정", 10);

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(savedAnswerId, request, session);

        // then
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_테스트_답변_수정_실패1() throws Exception {
        // given
        UpdateGroomingTestAnswerRequest request =
                new UpdateGroomingTestAnswerRequest(null, null, 10);

        // when
        MockHttpSession session = createUserAndLogin();
        ResultActions resultActions = getResultActions(savedAnswerId, request, session);

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHORIZATION.getHttpStatus().value()))
                .andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_테스트_답변_수정_실패2() throws Exception {
        // given
        UpdateGroomingTestAnswerRequest request =
                new UpdateGroomingTestAnswerRequest(null, null, 10);

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(1000000L, request, session);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.GROOMING_TEST_ANSWER_NOT_FOUND
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_테스트_답변_수정_실패3() throws Exception {
        // given
        UpdateGroomingTestAnswerRequest request =
                new UpdateGroomingTestAnswerRequest(100000000L, null, 10);

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(savedAnswerId, request, session);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.GROOMING_TEST_QUESTION_NOT_FOUND
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());
    }
}
