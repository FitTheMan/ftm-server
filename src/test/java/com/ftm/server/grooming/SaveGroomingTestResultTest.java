package com.ftm.server.grooming;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
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
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class SaveGroomingTestResultTest extends BaseTest {

    @Autowired private SecurityAuthenticationPort securityAuthenticationPort;
    @Autowired private SaveUserPort saveUserPort;
    @Autowired private SaveUserImagePort saveUserImagePort;
    @Autowired private LoadUserForAuthPort loadUserForAuthPort;

    private final List<FieldDescriptor> requestFieldSaveGroomingTestResult =
            List.of(
                    fieldWithPath("userId").type(NUMBER).description("유저 ID"),
                    fieldWithPath("groomingLevelId").type(NUMBER).description("그루밍 레벨 ID"),
                    fieldWithPath("totalScore").type(NUMBER).description("그루밍 테스트 총 점수"),
                    fieldWithPath("results[]").type(ARRAY).optional().description("그루밍 테스트 결과 목록"),
                    fieldWithPath("results[].questionId").type(NUMBER).description("질문 ID"),
                    fieldWithPath("results[].answerIds[]").type(ARRAY).description("선택한 답변 ID 목록"));

    private final List<FieldDescriptor> responseFieldSaveGroomingTestResult =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"));

    private ResultActions getResultActions(SaveGroomingTestResultRequest request) throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/grooming/tests")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "saveGroomingTestResult/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                requestFields(requestFieldSaveGroomingTestResult),
                responseFields(responseFieldSaveGroomingTestResult),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("그루밍 테스트")
                                .summary("그루밍 테스트 결과 저장 api")
                                .description("그루밍 테스트 결과 저장 api 입니다.")
                                .responseFields(responseFieldSaveGroomingTestResult)
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
    void 그루밍_테스트_결과_저장_성공() throws Exception {
        // given
        User user =
                loadUserForAuthPort.loadUserByEmail(FindByEmailQuery.of("test@gmail.com")).get();
        List<SaveGroomingTestResultRequest.GroomingTestResult> results =
                List.of(
                        new SaveGroomingTestResultRequest.GroomingTestResult(
                                1L, List.of(1L, 2L, 3L)));
        SaveGroomingTestResultRequest request =
                new SaveGroomingTestResultRequest(user.getId(), 1L, 10, results);

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions.andExpect(status().isCreated()).andDo(print());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 그루밍_테스트_결과_저장_실패1() throws Exception {
        // given
        List<SaveGroomingTestResultRequest.GroomingTestResult> results = new ArrayList<>();
        SaveGroomingTestResultRequest request =
                new SaveGroomingTestResultRequest(100L, 1L, 10, results);

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.USER_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.USER_NOT_FOUND.getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 그루밍_테스트_결과_저장_실패2() throws Exception {
        // given
        User user =
                loadUserForAuthPort.loadUserByEmail(FindByEmailQuery.of("test@gmail.com")).get();
        List<SaveGroomingTestResultRequest.GroomingTestResult> results = new ArrayList<>();
        SaveGroomingTestResultRequest request =
                new SaveGroomingTestResultRequest(user.getId(), 100L, 10, results);

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.GROOMING_LEVEL_NOT_FOUND
                                                .getHttpStatus()
                                                .value()))
                .andExpect(
                        jsonPath("code")
                                .value(ErrorResponseCode.GROOMING_LEVEL_NOT_FOUND.getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(3));
    }

    @Test
    @Transactional
    void 그루밍_테스트_결과_저장_실패3() throws Exception {
        // given
        User user =
                loadUserForAuthPort.loadUserByEmail(FindByEmailQuery.of("test@gmail.com")).get();
        List<SaveGroomingTestResultRequest.GroomingTestResult> results =
                List.of(new SaveGroomingTestResultRequest.GroomingTestResult(1000L, List.of()));
        SaveGroomingTestResultRequest request =
                new SaveGroomingTestResultRequest(user.getId(), 1L, 10, results);

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.INVALID_GROOMING_TEST_QUESTION_ID
                                                .getHttpStatus()
                                                .value()))
                .andExpect(
                        jsonPath("code")
                                .value(
                                        ErrorResponseCode.INVALID_GROOMING_TEST_QUESTION_ID
                                                .getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(4));
    }

    @Test
    @Transactional
    void 그루밍_테스트_결과_저장_실패4() throws Exception {
        // given
        User user =
                loadUserForAuthPort.loadUserByEmail(FindByEmailQuery.of("test@gmail.com")).get();
        List<SaveGroomingTestResultRequest.GroomingTestResult> results =
                List.of(new SaveGroomingTestResultRequest.GroomingTestResult(1L, List.of(100L)));
        SaveGroomingTestResultRequest request =
                new SaveGroomingTestResultRequest(user.getId(), 1L, 10, results);

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.INVALID_GROOMING_TEST_ANSWER_ID
                                                .getHttpStatus()
                                                .value()))
                .andExpect(
                        jsonPath("code")
                                .value(ErrorResponseCode.INVALID_GROOMING_TEST_ANSWER_ID.getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(5));
    }
}
