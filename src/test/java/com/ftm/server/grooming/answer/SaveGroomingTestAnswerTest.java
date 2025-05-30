package com.ftm.server.grooming.answer;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.grooming.dto.request.SaveGroomingTestAnswerRequest;
import com.ftm.server.adapter.out.persistence.mapper.GroomingTestQuestionMapper;
import com.ftm.server.adapter.out.persistence.repository.GroomingTestQuestionRepository;
import com.ftm.server.application.command.grooming.question.SaveGroomingTestQuestionCommand;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingTestQuestion;
import com.ftm.server.domain.enums.GroomingCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class SaveGroomingTestAnswerTest extends BaseTest {

    @Autowired private GroomingTestQuestionRepository groomingTestQuestionRepository;
    @Autowired private GroomingTestQuestionMapper groomingTestQuestionMapper;

    private Long savedQuestionId;

    private ResultActions getResultActions(
            Long questionId, SaveGroomingTestAnswerRequest request, MockHttpSession session)
            throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.post(
                                "/api/grooming/tests/questions/{questionId}/answers", questionId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request))
                        .session(session));
    }

    @BeforeEach
    void setUp() {
        SaveGroomingTestQuestionCommand command =
                SaveGroomingTestQuestionCommand.of(GroomingCategory.BEAUTY, "그루밍 테스트 질문");
        GroomingTestQuestion question = GroomingTestQuestion.create(command);
        savedQuestionId =
                groomingTestQuestionRepository
                        .save(groomingTestQuestionMapper.toJpaEntity(question))
                        .getId();
    }

    @Test
    @Transactional
    void 그루밍_테스트_답변_저장_성공() throws Exception {
        // given
        SaveGroomingTestAnswerRequest request = new SaveGroomingTestAnswerRequest("그루밍 테스트 답변", 1);

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(savedQuestionId, request, session);

        // then
        resultActions.andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_테스트_답변_저장_실패1() throws Exception {
        // given
        SaveGroomingTestAnswerRequest request = new SaveGroomingTestAnswerRequest("그루밍 테스트 답변", 1);

        // when
        MockHttpSession session = createUserAndLogin();
        ResultActions resultActions = getResultActions(savedQuestionId, request, session);

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHORIZATION.getHttpStatus().value()))
                .andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_테스트_답변_저장_실패2() throws Exception {
        // given
        SaveGroomingTestAnswerRequest request = new SaveGroomingTestAnswerRequest("그루밍 테스트 답변", 1);

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(10000000L, request, session);

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
