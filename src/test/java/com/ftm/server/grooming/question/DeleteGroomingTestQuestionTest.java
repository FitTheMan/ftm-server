package com.ftm.server.grooming.question;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ftm.server.BaseTest;
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

public class DeleteGroomingTestQuestionTest extends BaseTest {

    @Autowired private GroomingTestQuestionRepository groomingTestQuestionRepository;
    @Autowired private GroomingTestQuestionMapper groomingTestQuestionMapper;

    private Long questionId;

    private ResultActions getResultActions(Long questionId, MockHttpSession session)
            throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.delete(
                                "/api/grooming/tests/questions/{questionId}", questionId)
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
    void 그루밍_테스트_질문_삭제_성공() throws Exception {
        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(questionId, session);

        // given
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_테스트_질문_삭제_실패() throws Exception {
        // when
        MockHttpSession session = createUserAndLogin();
        ResultActions resultActions = getResultActions(questionId, session);

        // given
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHORIZATION.getHttpStatus().value()))
                .andDo(print());
    }
}
