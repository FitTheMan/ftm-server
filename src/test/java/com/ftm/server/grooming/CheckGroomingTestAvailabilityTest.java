package com.ftm.server.grooming;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.auth.dto.request.GeneralLoginRequest;
import com.ftm.server.application.command.user.GeneralUserCreationCommand;
import com.ftm.server.application.port.out.persistence.auth.LoadUserForAuthPort;
import com.ftm.server.application.port.out.persistence.grooming.SaveGroomingTestResultPort;
import com.ftm.server.application.port.out.persistence.user.SaveUserImagePort;
import com.ftm.server.application.port.out.persistence.user.SaveUserPort;
import com.ftm.server.application.port.out.security.SecurityAuthenticationPort;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingTestResult;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import java.time.LocalDateTime;
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

public class CheckGroomingTestAvailabilityTest extends BaseTest {

    @Autowired private SecurityAuthenticationPort securityAuthenticationPort;
    @Autowired private SaveUserPort saveUserPort;
    @Autowired private SaveUserImagePort saveUserImagePort;
    @Autowired private LoadUserForAuthPort loadUserForAuthPort;
    @Autowired private SaveGroomingTestResultPort saveGroomingTestResultPort;

    private final List<FieldDescriptor> responseFieldCheckGroomingTestAvailability =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("data"),
                    fieldWithPath("data.available")
                            .type(BOOLEAN)
                            .description("그루밍 테스트 가능 여부 (true일 경우 테스트 가능)"),
                    fieldWithPath("data.remainingDays")
                            .type(NUMBER)
                            .optional()
                            .description("다음 테스트까지 남은 일수"),
                    fieldWithPath("data.lastTestedAt")
                            .type(STRING)
                            .optional()
                            .description("마지막으로 테스트한 날짜"),
                    fieldWithPath("data.nextAvailableAt")
                            .type(STRING)
                            .optional()
                            .description("테스트가 가능해지는 날짜"));

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "checkGroomingTestAvailability/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                requestHeaders(
                        headerWithName("Cookie")
                                .description("로그인 시 발급받은 SESSION Cookie")
                                .optional()),
                responseFields(responseFieldCheckGroomingTestAvailability),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("그루밍 테스트")
                                .summary("그루밍 테스트 가능 여부 api")
                                .description("그루밍 테스트 가능 여부를 조회하는 api 입니다.")
                                .responseFields(responseFieldCheckGroomingTestAvailability)
                                .build()));
    }

    private MockHttpSession getLoginResultActions() throws Exception {
        GeneralLoginRequest request = new GeneralLoginRequest("test@gmail.com", "test1234!");

        MvcResult loginResult =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.post("/api/auth/login")
                                        .contentType(APPLICATION_JSON_VALUE)
                                        .content(mapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andReturn();

        return (MockHttpSession) loginResult.getRequest().getSession(false);
    }

    private ResultActions getResultActions(MockHttpSession session) throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/grooming/tests/availability")
                        .session(session));
    }

    @BeforeEach
    void setUp() {
        GeneralUserCreationCommand command =
                new GeneralUserCreationCommand(
                        "test@gmail.com",
                        securityAuthenticationPort.passwordEncode("test1234!"),
                        "test",
                        null,
                        null);
        User testUser = saveUserPort.saveUser(User.createGeneralUser(command));
        saveUserImagePort.saveUserDefaultImage(UserImage.createUserImage(testUser.getId()));
    }

    @Test
    @Transactional
    void 그루밍_테스트_가능_여부_조회_성공1() throws Exception {
        // given
        User user =
                loadUserForAuthPort.loadUserByEmail(FindByEmailQuery.of("test@gmail.com")).get();
        List<GroomingTestResult> results =
                List.of(
                        GroomingTestResult.create(user.getId(), 1L, 1L, LocalDateTime.now()),
                        GroomingTestResult.create(
                                user.getId(), 1L, 1L, LocalDateTime.now().minusDays(7)));
        saveGroomingTestResultPort.saveGroomingTestResults(results);

        // when
        MockHttpSession session = getLoginResultActions();
        ResultActions resultActions = getResultActions(session);
        resultActions
                .andReturn()
                .getRequest()
                .addHeader("Cookie", "SESSION=mock-session-id; Path=/; HttpOnly");

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 그루밍_테스트_가능_여부_조회_성공2() throws Exception {
        // given
        User user =
                loadUserForAuthPort.loadUserByEmail(FindByEmailQuery.of("test@gmail.com")).get();
        List<GroomingTestResult> results =
                List.of(
                        GroomingTestResult.create(
                                user.getId(), 1L, 1L, LocalDateTime.now().minusDays(7)));
        saveGroomingTestResultPort.saveGroomingTestResults(results);

        // when
        MockHttpSession session = getLoginResultActions();
        ResultActions resultActions = getResultActions(session);
        resultActions
                .andReturn()
                .getRequest()
                .addHeader("Cookie", "SESSION=mock-session-id; Path=/; HttpOnly");

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // documentation
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 그루밍_테스트_가능_여부_조회_성공3() throws Exception {
        // when
        MockHttpSession session = getLoginResultActions();
        ResultActions resultActions = getResultActions(session);
        resultActions
                .andReturn()
                .getRequest()
                .addHeader("Cookie", "SESSION=mock-session-id; Path=/; HttpOnly");

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // documentation
        resultActions.andDo(getDocument(3));
    }

    @Test
    @Transactional
    void 그루밍_테스트_가능_여부_조회_실패() throws Exception {
        // when
        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/grooming/tests/availability"));

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHENTICATED.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.NOT_AUTHENTICATED.getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(4));
    }
}
