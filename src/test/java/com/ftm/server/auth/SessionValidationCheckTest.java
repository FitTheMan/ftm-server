package com.ftm.server.auth;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;

public class SessionValidationCheckTest extends BaseTest {

    private final List<FieldDescriptor> responseFieldDescriptors =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("data"),
                    fieldWithPath("data.isValid").type(BOOLEAN).description("session 유효성 여부"));

    private ResultActions getResultActions(MockHttpSession session) throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.get("/api/auth/session/validity")
                        .session(session)
                        .cookie(new Cookie("SESSION", session.getId())));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "sessionValidityCheck/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldDescriptors),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증/인가")
                                .summary("세션 유효성 확인 api")
                                .description("세션 유효성 확인 api입니다.")
                                .responseFields(responseFieldDescriptors)
                                .build()));
    }

    @Test
    @Transactional
    public void 세션_유효성_성공1() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();

        // when
        ResultActions resultActions = getResultActions(session);

        // then
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("data.isValid").value(true));
        ;

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    public void 세션_유효성_성공2() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();
        session.invalidate();

        // when
        ResultActions resultActions = getResultActions(session);

        // then
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("data.isValid").value(false));

        // documentation
        resultActions.andDo(getDocument(2));
    }
}
