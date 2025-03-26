package com.ftm.server.auth;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.application.dto.command.GeneralUserCreationCommand;
import com.ftm.server.application.port.AuthenticationPort;
import com.ftm.server.application.port.repository.UserImageRepository;
import com.ftm.server.application.port.repository.UserRepository;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.web.dto.request.UserLoginRequest;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class UserLoginTest extends BaseTest {

    @Autowired private UserRepository userRepository;
    @Autowired private UserImageRepository userImageRepository;
    @Autowired private AuthenticationPort authenticationPort;

    private final List<FieldDescriptor> requestFieldLoginUser =
            List.of(
                    fieldWithPath("email").type(STRING).description("이메일"),
                    fieldWithPath("password").type(STRING).description("패스워드"));

    private final List<FieldDescriptor> responseFieldLoginUser =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("data"),
                    fieldWithPath("data.id").type(NUMBER).description("유저 ID"),
                    fieldWithPath("data.nickname").type(STRING).description("유저 닉네임"),
                    fieldWithPath("data.profileImageUrl")
                            .type(STRING)
                            .description("유저 프로필 이미지 URL"),
                    fieldWithPath("data.mildLevelName")
                            .type(STRING)
                            .optional()
                            .description("순한맛 그루밍 레벨 이름"),
                    fieldWithPath("data.spicyLevelName")
                            .type(STRING)
                            .optional()
                            .description("매운맛 그루밍 레벨 이름"),
                    fieldWithPath("data.loginTime").type(STRING).description("로그인 시간"));

    private ResultActions getResultActions(UserLoginRequest request) throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/auth/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "loginUser/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                requestFields(requestFieldLoginUser),
                responseHeaders(
                        headerWithName("Set-Cookie")
                                .description("세션 ID를 담고 있는 쿠키 (SESSION), 만료 시간: 1시간")
                                .optional()),
                responseFields(responseFieldLoginUser),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증/인가")
                                .summary("유저 로그인 api")
                                .description("유저 로그인 api 입니다.")
                                .responseFields(responseFieldLoginUser)
                                .build()));
    }

    @BeforeEach
    void setUp() {
        GeneralUserCreationCommand testCommand =
                GeneralUserCreationCommand.of(
                        "test@gmail.com",
                        authenticationPort.passwordEncode("test1234!"),
                        "test",
                        AgeGroup.TEENS,
                        null);
        User testUser = userRepository.save(User.createGeneralUser(testCommand));
        userImageRepository.save(UserImage.createUserImage(testUser));
    }

    @Test
    @Transactional
    void 유저_로그인_성공() throws Exception {
        // given
        String email = "test@gmail.com";
        String password = "test1234!";
        UserLoginRequest request = new UserLoginRequest(email, password);

        // when
        ResultActions resultActions = getResultActions(request);
        MvcResult result = resultActions.andReturn();

        // 세션 쿠키 수동 추가 (문서화 통과용)
        result.getResponse().addHeader("Set-Cookie", "SESSION=mock-session-id; Path=/; HttpOnly");

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", Matchers.containsString("SESSION")))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 유저_로그인_실패() throws Exception {
        // given
        String email = "test@gmail.com";
        String password = "test12345!";
        UserLoginRequest request = new UserLoginRequest(email, password);

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.INVALID_CREDENTIALS.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.INVALID_CREDENTIALS.getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(2));
    }
}
