package com.ftm.server.grooming;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.auth.dto.request.GeneralLoginRequest;
import com.ftm.server.adapter.in.web.grooming.dto.request.GroomingTestSubmissionRequest;
import com.ftm.server.application.command.user.GeneralUserCreationCommand;
import com.ftm.server.application.port.out.persistence.user.SaveUserImagePort;
import com.ftm.server.application.port.out.persistence.user.SaveUserPort;
import com.ftm.server.application.port.out.security.SecurityAuthenticationPort;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class SubmitGroomingTestsTest extends BaseTest {

    @Autowired private SecurityAuthenticationPort securityAuthenticationPort;
    @Autowired private SaveUserPort saveUserPort;
    @Autowired private SaveUserImagePort saveUserImagePort;

    private final List<FieldDescriptor> requestFieldSubmitGroomingTests =
            List.of(
                    fieldWithPath("submissions").type(ARRAY).description("그루밍 테스트 제출 목록"),
                    fieldWithPath("submissions[].questionId").type(NUMBER).description("질문 ID"),
                    fieldWithPath("submissions[].groomingCategory")
                            .type(STRING)
                            .description("질문 그루밍 카테고리"),
                    fieldWithPath("submissions[].answers[]")
                            .type(ARRAY)
                            .description("질문에 답한 응답 목록"),
                    fieldWithPath("submissions[].answers[].answerId")
                            .type(NUMBER)
                            .description("응답 ID"),
                    fieldWithPath("submissions[].answers[].score")
                            .type(NUMBER)
                            .description("응답 점수"));

    private final List<FieldDescriptor> responseFieldSubmitGroomingTests =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"),
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
                    fieldWithPath("data.level.mildLevelName")
                            .type(STRING)
                            .description("그루밍 레벨 순한맛 이름"),
                    fieldWithPath("data.level.spicyLevelName")
                            .type(STRING)
                            .description("그루밍 레벨 매운맛 이름"));

    private GroomingTestSubmissionRequest getRequest() {
        List<GroomingTestSubmissionRequest.SubmittedQuestion> submissions =
                List.of(
                        new GroomingTestSubmissionRequest.SubmittedQuestion(
                                1L,
                                "BEAUTY",
                                List.of(
                                        new GroomingTestSubmissionRequest.SubmittedQuestion
                                                .SelectedAnswer(1L, 1),
                                        new GroomingTestSubmissionRequest.SubmittedQuestion
                                                .SelectedAnswer(2L, 1),
                                        new GroomingTestSubmissionRequest.SubmittedQuestion
                                                .SelectedAnswer(3L, 2))),
                        new GroomingTestSubmissionRequest.SubmittedQuestion(
                                6L,
                                "HYGIENE",
                                List.of(
                                        new GroomingTestSubmissionRequest.SubmittedQuestion
                                                .SelectedAnswer(16L, 1))),
                        new GroomingTestSubmissionRequest.SubmittedQuestion(
                                10L,
                                "HAIR",
                                List.of(
                                        new GroomingTestSubmissionRequest.SubmittedQuestion
                                                .SelectedAnswer(28L, 1))),
                        new GroomingTestSubmissionRequest.SubmittedQuestion(
                                15L,
                                "WORKOUT",
                                List.of(
                                        new GroomingTestSubmissionRequest.SubmittedQuestion
                                                .SelectedAnswer(41L, 1),
                                        new GroomingTestSubmissionRequest.SubmittedQuestion
                                                .SelectedAnswer(42L, 1),
                                        new GroomingTestSubmissionRequest.SubmittedQuestion
                                                .SelectedAnswer(43L, 1),
                                        new GroomingTestSubmissionRequest.SubmittedQuestion
                                                .SelectedAnswer(44L, 1))),
                        new GroomingTestSubmissionRequest.SubmittedQuestion(
                                18L,
                                "FASHION",
                                List.of(
                                        new GroomingTestSubmissionRequest.SubmittedQuestion
                                                .SelectedAnswer(61L, 4))));
        return new GroomingTestSubmissionRequest(submissions);
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "submitGroomingTests/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                requestFields(requestFieldSubmitGroomingTests),
                responseFields(responseFieldSubmitGroomingTests),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("그루밍 테스트")
                                .summary("그루밍 테스트 제출 api")
                                .description("그루밍 테스트 제출 api 입니다.")
                                .responseFields(responseFieldSubmitGroomingTests)
                                .build()));
    }

    @Test
    @Transactional
    void 그루밍_테스트_제출_성공1() throws Exception {
        // given
        GeneralUserCreationCommand command =
                new GeneralUserCreationCommand(
                        "test@gmail.com",
                        securityAuthenticationPort.passwordEncode("test1234!"),
                        "test",
                        null,
                        null);
        User testUser = saveUserPort.saveUser(User.createGeneralUser(command));
        saveUserImagePort.saveUserDefaultImage(UserImage.createUserImage(testUser.getId()));
        GroomingTestSubmissionRequest request = getRequest();

        // when
        MvcResult loginResult =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.post("/api/auth/login")
                                        .contentType(APPLICATION_JSON_VALUE)
                                        .content(
                                                mapper.writeValueAsString(
                                                        new GeneralLoginRequest(
                                                                "test@gmail.com", "test1234!"))))
                        .andExpect(status().isOk())
                        .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/grooming/tests/submission")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(request))
                                .session(session));
        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 그루밍_테스트_제출_성공2() throws Exception {
        // given
        // given
        GroomingTestSubmissionRequest request = getRequest();

        // when
        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/grooming/tests/submission")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(request)));

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // documentation
        resultActions.andDo(getDocument(2));
    }
}
