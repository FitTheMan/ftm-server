package com.ftm.server.grooming.level;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.grooming.dto.request.SaveGroomingLevelRequest;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class SaveGroomingLevelTest extends BaseTest {

    private ResultActions getResultActions(
            SaveGroomingLevelRequest request, MockHttpSession session) throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/grooming/levels")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request))
                        .session(session));
    }

    @Test
    @Transactional
    void 그루밍_레벨_저장_성공() throws Exception {
        // given
        SaveGroomingLevelRequest request =
                new SaveGroomingLevelRequest(0, 10, "테스트 순한맛", "테스트 매운맛");

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(request, session);

        // given
        resultActions.andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_레벨_저장_실패1() throws Exception {
        // given
        SaveGroomingLevelRequest request =
                new SaveGroomingLevelRequest(0, 10, "테스트 순한맛", "테스트 매운맛");

        // when
        MockHttpSession session = createUserAndLogin();
        ResultActions resultActions = getResultActions(request, session);

        // given
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHORIZATION.getHttpStatus().value()))
                .andDo(print());
    }

    @Test
    @Transactional
    void 그루밍_레벨_저장_실패2() throws Exception {
        // given
        SaveGroomingLevelRequest request =
                new SaveGroomingLevelRequest(-1, 10, "테스트 순한맛", "테스트 매운맛");

        // when
        MockHttpSession session = createAdminUserAndLogin();
        ResultActions resultActions = getResultActions(request, session);

        // given
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.INVALID_REQUEST_ARGUMENT
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());
    }
}
