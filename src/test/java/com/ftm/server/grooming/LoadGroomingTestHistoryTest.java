package com.ftm.server.grooming;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.auth.dto.request.GeneralLoginRequest;
import com.ftm.server.adapter.in.web.grooming.dto.request.SaveGroomingTestResultRequest;
import com.ftm.server.application.command.user.GeneralUserCreationCommand;
import com.ftm.server.application.port.out.persistence.auth.LoadUserForAuthPort;
import com.ftm.server.application.port.out.persistence.user.SaveUserImagePort;
import com.ftm.server.application.port.out.persistence.user.SaveUserPort;
import com.ftm.server.application.port.out.security.SecurityAuthenticationPort;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
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

public class LoadGroomingTestHistoryTest extends BaseTest {

    @Autowired private SecurityAuthenticationPort securityAuthenticationPort;
    @Autowired private SaveUserPort saveUserPort;
    @Autowired private SaveUserImagePort saveUserImagePort;
    @Autowired private LoadUserForAuthPort loadUserForAuthPort;

    private final List<FieldDescriptor> responseFieldLoadGroomingTestHistory =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("data"),
                    fieldWithPath("data.historyDates[]")
                            .type(ARRAY)
                            .description("그루밍 테스트 이력 날짜 목록 (최신순)"));

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "loadGroomingTestHistory/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                requestHeaders(
                        headerWithName("Cookie")
                                .description("로그인 시 발급받은 SESSION Cookie")
                                .optional()),
                responseFields(responseFieldLoadGroomingTestHistory),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("그루밍 테스트")
                                .summary("유저 그루밍 테스트 이력 목록 조회 api")
                                .description("유저 그루밍 테스트 이력 목록 조회 api 입니다.")
                                .responseFields(responseFieldLoadGroomingTestHistory)
                                .build()));
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
    void 그루밍_테스트_이력_목록_조회_성공() throws Exception {
        // given
        GeneralLoginRequest loginRequest = new GeneralLoginRequest("test@gmail.com", "test1234!");
        User user =
                loadUserForAuthPort.loadUserByEmail(FindByEmailQuery.of("test@gmail.com")).get();
        List<SaveGroomingTestResultRequest.GroomingTestResult> results =
                List.of(
                        new SaveGroomingTestResultRequest.GroomingTestResult(
                                1L, List.of(1L, 2L, 3L)),
                        new SaveGroomingTestResultRequest.GroomingTestResult(2L, List.of(5L)));
        SaveGroomingTestResultRequest saveGroomingTestResultRequest =
                new SaveGroomingTestResultRequest(user.getId(), 1L, 10, results);

        // when
        MvcResult loginResult =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.post("/api/auth/login")
                                        .contentType(APPLICATION_JSON_VALUE)
                                        .content(mapper.writeValueAsString(loginRequest)))
                        .andExpect(status().isOk())
                        .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/grooming/tests")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(saveGroomingTestResultRequest)));

        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/grooming/tests/histories")
                                .session(session));

        MvcResult result = resultActions.andReturn();
        result.getRequest().addHeader("Cookie", "SESSION=mock-session-id; Path=/; HttpOnly");

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 그루밍_테스트_이력_목록_조회_실패() throws Exception {
        // when
        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/grooming/tests/histories"));

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHENTICATED.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.NOT_AUTHENTICATED.getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(2));
    }
}
