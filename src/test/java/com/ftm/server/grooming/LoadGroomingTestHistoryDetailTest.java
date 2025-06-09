package com.ftm.server.grooming;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class LoadGroomingTestHistoryDetailTest extends BaseTest {

    @Autowired private SecurityAuthenticationPort securityAuthenticationPort;
    @Autowired private SaveUserPort saveUserPort;
    @Autowired private SaveUserImagePort saveUserImagePort;
    @Autowired private LoadUserForAuthPort loadUserForAuthPort;

    private final ParameterDescriptor queryParametersForDate =
            parameterWithName("date")
                    .description("특정 그루밍 테스트 날짜")
                    .attributes(
                            new Attributes.Attribute("constraint", "yyyy.MM.dd or yyyy-MM-dd 형식"));

    private final List<FieldDescriptor> responseFieldLoadGroomingTestHistoryDetail =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"),
                    fieldWithPath("data.testedAt").type(STRING).description("그루밍 테스트 날짜"),
                    fieldWithPath("data.scores").type(OBJECT).description("그루밍 테스트 카테고리 별 결과 점수"),
                    fieldWithPath("data.scores.beautyScore").type(NUMBER).description("뷰티 영역 점수"),
                    fieldWithPath("data.scores.hygieneScore").type(NUMBER).description("위생 영역 점수"),
                    fieldWithPath("data.scores.hairScore").type(NUMBER).description("미용 영역 점수"),
                    fieldWithPath("data.scores.workoutScore").type(NUMBER).description("운동 영역 점수"),
                    fieldWithPath("data.scores.fashionScore").type(NUMBER).description("패션 영역 점수"),
                    fieldWithPath("data.scores.totalScore").type(NUMBER).description("총 점수"),
                    fieldWithPath("data.grades").type(OBJECT).description("그루밍 테스트 카테고리 별 결과 등급"),
                    fieldWithPath("data.grades.beauty").type(OBJECT).description("뷰티 영역 등급"),
                    fieldWithPath("data.grades.beauty.grade")
                            .type(STRING)
                            .description("뷰티 영역 등급 이름"),
                    fieldWithPath("data.grades.beauty.level")
                            .type(NUMBER)
                            .description("뷰티 영역 등급 레벨(1, 2, 3)"),
                    fieldWithPath("data.grades.hygiene").type(OBJECT).description("위생 영역 등급"),
                    fieldWithPath("data.grades.hygiene.grade")
                            .type(STRING)
                            .description("위생 영역 등급 이름"),
                    fieldWithPath("data.grades.hygiene.level")
                            .type(NUMBER)
                            .description("위생 영역 등급 레벨(1, 2, 3)"),
                    fieldWithPath("data.grades.hair").type(OBJECT).description("미용 영역 등급"),
                    fieldWithPath("data.grades.hair.grade").type(STRING).description("미용 영역 등급 이름"),
                    fieldWithPath("data.grades.hair.level")
                            .type(NUMBER)
                            .description("미용 영역 등급 레벨(1, 2, 3)"),
                    fieldWithPath("data.grades.workout").type(OBJECT).description("운동 영역 등급"),
                    fieldWithPath("data.grades.workout.grade")
                            .type(STRING)
                            .description("운동 영역 등급 이름"),
                    fieldWithPath("data.grades.workout.level")
                            .type(NUMBER)
                            .description("운동 영역 등급 레벨(1, 2, 3)"),
                    fieldWithPath("data.grades.fashion").type(OBJECT).description("패션 영역 등급"),
                    fieldWithPath("data.grades.fashion.grade")
                            .type(STRING)
                            .description("패션 영역 등급 이름"),
                    fieldWithPath("data.grades.fashion.level")
                            .type(NUMBER)
                            .description("패션 영역 등급 레벨(1, 2, 3)"),
                    fieldWithPath("data.level").type(OBJECT).description("그루밍 테스트 결과 레벨"),
                    fieldWithPath("data.level.groomingLevelId")
                            .type(NUMBER)
                            .description("그루밍 레벨 ID"),
                    fieldWithPath("data.level.normalMode").type(OBJECT).description("그루밍 레벨 일반모드"),
                    fieldWithPath("data.level.normalMode.name")
                            .type(STRING)
                            .description("그루밍 레벨 일반모드 이름"),
                    fieldWithPath("data.level.normalMode.summary")
                            .type(STRING)
                            .description("그루밍 레벨 일반모드 요약"),
                    fieldWithPath("data.level.normalMode.description")
                            .type(STRING)
                            .description("그루밍 레벨 일반모드 설명"),
                    fieldWithPath("data.level.truthMode").type(OBJECT).description("그루밍 레벨 진심모드"),
                    fieldWithPath("data.level.truthMode.name")
                            .type(STRING)
                            .description("그루밍 레벨 진심모드 이름"),
                    fieldWithPath("data.level.truthMode.summary")
                            .type(STRING)
                            .description("그루밍 레벨 진심모드 요약"),
                    fieldWithPath("data.level.truthMode.description")
                            .type(STRING)
                            .description("그루밍 레벨 진심모드 설명"));

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "loadGroomingTestHistoryDetail/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                requestHeaders(
                        headerWithName("Cookie")
                                .description("로그인 시 발급받은 SESSION Cookie")
                                .optional()),
                responseFields(responseFieldLoadGroomingTestHistoryDetail),
                queryParameters(queryParametersForDate),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("그루밍 테스트")
                                .summary("유저 그루밍 테스트 이력 상세 조회 api")
                                .description("유저 그루밍 테스트 이력 상세 조회 api 입니다.")
                                .responseFields(responseFieldLoadGroomingTestHistoryDetail)
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
    void 그루밍_테스트_이력_상세_조회_성공() throws Exception {
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

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

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
                        RestDocumentationRequestBuilders.get("/api/grooming/tests/histories/detail")
                                .param("date", today)
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
    void 그루밍_테스트_이력_상세_조회_실패1() throws Exception {
        // when
        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/grooming/tests/histories/detail")
                                .param("date", "2025.04.10"));

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHENTICATED.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.NOT_AUTHENTICATED.getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 그루밍_테스트_이력_상세_조회_실패2() throws Exception {
        // given
        GeneralLoginRequest loginRequest = new GeneralLoginRequest("test@gmail.com", "test1234!");

        // when
        MvcResult loginResult =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.post("/api/auth/login")
                                        .contentType(APPLICATION_JSON_VALUE)
                                        .content(mapper.writeValueAsString(loginRequest)))
                        .andExpect(status().isOk())
                        .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/grooming/tests/histories/detail")
                                .param("date", "2025/04/10 13:00")
                                .session(session));

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.INVALID_REQUEST_ARGUMENT
                                                .getHttpStatus()
                                                .value()))
                .andExpect(
                        jsonPath("code")
                                .value(ErrorResponseCode.INVALID_REQUEST_ARGUMENT.getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(3));
    }
}
