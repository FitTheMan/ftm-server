package com.ftm.server.grooming.question;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.grooming.dto.request.UpdateGroomingTestQuestionRequest;
import com.ftm.server.adapter.out.persistence.mapper.GroomingTestQuestionMapper;
import com.ftm.server.adapter.out.persistence.repository.GroomingTestQuestionRepository;
import com.ftm.server.application.command.grooming.SaveGroomingTestQuestionCommand;
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

public class UpdateGroomingTestQuestionTest extends BaseTest {

    @Autowired private GroomingTestQuestionRepository groomingTestQuestionRepository;
    @Autowired private GroomingTestQuestionMapper groomingTestQuestionMapper;

    private Long questionId;

    private ResultActions getResultActions(
            Long questionId, UpdateGroomingTestQuestionRequest request, MockHttpSession session)
            throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.patch(
                                "/api/grooming/tests/questions/{questionId}", questionId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request))
                        .session(session));
    }

    @BeforeEach
    void setUp() {
        SaveGroomingTestQuestionCommand command =
                SaveGroomingTestQuestionCommand.of(GroomingCategory.BEAUTY, "그루밍 테스트 질문");
        GroomingTestQuestion question = GroomingTestQuestion.create(command);
        questionId =
                groomingTestQuestionRepository
                        .save(groomingTestQuestionMapper.toJpaEntity(question))
                        .getId();
    }

    @Test
    @Transactional
    void 그루밍_테스트_질문_수정_성공() throws Exception {
        // given
        UpdateGroomingTestQuestionRequest request =
                new UpdateGroomingTestQuestionRequest(null, "질문 수정");

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(questionId, request, session);

        // given
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_테스트_질문_수정_실패1() throws Exception {
        // given
        UpdateGroomingTestQuestionRequest request =
                new UpdateGroomingTestQuestionRequest(null, "질문 수정");

        // when
        MockHttpSession session = createUserAndLogin();
        ResultActions resultActions = getResultActions(questionId, request, session);

        // given
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHORIZATION.getHttpStatus().value()))
                .andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_테스트_질문_수정_실패2() throws Exception {
        // given
        UpdateGroomingTestQuestionRequest request =
                new UpdateGroomingTestQuestionRequest(null, "질문 수정");

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(1000000L, request, session);

        // given
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.GROOMING_TEST_QUESTION_NOT_FOUND
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());
    }
}
