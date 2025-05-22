package com.ftm.server.grooming.question;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.grooming.dto.request.SaveGroomingTestQuestionRequest;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.enums.GroomingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class SaveGroomingTestQuestionTest extends BaseTest {

    private ResultActions getResultActions(
            SaveGroomingTestQuestionRequest request, MockHttpSession session) throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/grooming/tests/questions")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request))
                        .session(session));
    }

    @Test
    @Transactional
    void 그루밍_테스트_질문_저장_성공() throws Exception {
        // given
        SaveGroomingTestQuestionRequest request =
                new SaveGroomingTestQuestionRequest(GroomingCategory.BEAUTY, "테스트 질문입니다.");

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(request, session);

        // given
        resultActions.andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_테스트_질문_저장_실패() throws Exception {
        // given
        SaveGroomingTestQuestionRequest request =
                new SaveGroomingTestQuestionRequest(GroomingCategory.BEAUTY, "테스트 질문입니다.");

        // when
        MockHttpSession session = createUserAndLogin();
        ResultActions resultActions = getResultActions(request, session);

        // given
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHORIZATION.getHttpStatus().value()))
                .andDo(print());
    }
}
