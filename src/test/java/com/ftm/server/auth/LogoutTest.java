package com.ftm.server.auth;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.application.dto.command.GeneralUserCreationCommand;
import com.ftm.server.application.port.AuthenticationPort;
import com.ftm.server.application.port.repository.UserImageRepository;
import com.ftm.server.application.port.repository.UserRepository;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import com.ftm.server.web.dto.request.UserLoginRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class LogoutTest extends BaseTest {

    @Autowired private UserRepository userRepository;

    @Autowired private UserImageRepository userImageRepository;

    @Autowired private AuthenticationPort authenticationPort;

    private final List<FieldDescriptor> responseFieldLogout =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("data"));

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "logout/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                requestHeaders(
                        headerWithName("Cookie")
                                .description("로그인 시 발급받은 SESSION Cookie")
                                .optional()),
                responseFields(responseFieldLogout),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증/인가")
                                .summary("로그아웃 api")
                                .description("로그아웃 api 입니다.")
                                .responseFields(responseFieldLogout)
                                .build()));
    }

    @BeforeEach
    void setUp() {
        GeneralUserCreationCommand command =
                new GeneralUserCreationCommand(
                        "test@gmail.com",
                        authenticationPort.passwordEncode("test1234!"),
                        "test",
                        null,
                        null);
        User testUser = User.createGeneralUser(command);
        User saved = userRepository.save(testUser);
        userImageRepository.save(UserImage.createUserImage(saved));
    }

    @Test
    @Transactional
    void 로그아웃_성공() throws Exception {
        // given
        UserLoginRequest request = new UserLoginRequest("test@gmail.com", "test1234!");

        // when
        MvcResult loginResult =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.post("/api/auth/login")
                                        .contentType(APPLICATION_JSON_VALUE)
                                        .content(mapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/logout").session(session));

        // 요청 헤더에 세션 쿠키 수동 추가 (문서화 통과용)
        MvcResult result = resultActions.andReturn();
        result.getRequest().addHeader("Cookie", "SESSION=mock-session-id; Path=/; HttpOnly");

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 로그아웃_실패() throws Exception {
        // when
        ResultActions resultActions =
                mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/logout"));

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHENTICATED.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.NOT_AUTHENTICATED.getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(2));
    }
}
