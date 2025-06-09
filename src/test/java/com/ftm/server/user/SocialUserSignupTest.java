package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.ftm.server.common.consts.StaticConsts.PENDING_SOCIAL_USER_SESSION_KEY;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.user.dto.request.SocialUserSignupRequest;
import com.ftm.server.application.command.user.SocialUserCreationCommand;
import com.ftm.server.application.port.out.persistence.user.SaveUserPort;
import com.ftm.server.application.vo.auth.PendingSocialUserVo;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import com.ftm.server.domain.enums.SocialProvider;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

public class SocialUserSignupTest extends BaseTest {

    @Autowired private SaveUserPort saveUserPort;

    private final List<FieldDescriptor> requestFieldDescriptors =
            List.of(
                    fieldWithPath("age")
                            .type(JsonFieldType.STRING)
                            .description("연령대. 사용자 정보 옵션 조회 api에서 반환받은 value 값을 전달해 주세요."),
                    fieldWithPath("hashtags")
                            .type(JsonFieldType.ARRAY)
                            .description("관심 해시태그. 사용자 정보 옵션 조회 api에서 반환받은 value 값을 전달해 주세요.")
                            .optional());

    private final List<FieldDescriptor> responseFieldDescriptors =
            List.of(
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("data"),
                    fieldWithPath("data.id")
                            .type(JsonFieldType.NUMBER)
                            .description("생성된 회원의 고유 id"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                    fieldWithPath("data.socialProvider").type(STRING).description("소셜 제공자"),
                    fieldWithPath("data.profileImageUrl")
                            .type(JsonFieldType.STRING)
                            .description("프로필 이미지 url"),
                    fieldWithPath("data.normalLevelName")
                            .type(JsonFieldType.STRING)
                            .description("그루밍 레벨 일반모드 이름")
                            .optional()
                            .attributes(
                                    new Attributes.Attribute(
                                            "nullable", "그루밍 테스트를 아직 진행하지 않은 경우 null")),
                    fieldWithPath("data.truthLevelName")
                            .type(JsonFieldType.STRING)
                            .description("그루밍 레벨 진심모드 이름")
                            .optional()
                            .attributes(
                                    new Attributes.Attribute(
                                            "nullable", "그루밍 테스트를 아직 진행하지 않은 경우 null")),
                    fieldWithPath("data.loginTime").type(STRING).description("로그인 시간"));

    private ResultActions getResultActions(SocialUserSignupRequest request, MockHttpSession session)
            throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.post("/api/users/social")
                        .contentType(MediaType.APPLICATION_JSON) // request body content type
                        .session(session)
                        .cookie(new Cookie("SESSION", session.getId()))
                        .content(mapper.writeValueAsString(request)));
    }

    // 문서화 반환 함수
    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "socialUserSignUp/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldDescriptors),
                requestFields(requestFieldDescriptors),
                responseHeaders(
                        headerWithName("Set-Cookie")
                                .description(
                                        "로그인 성공 시 발급되는 세션 쿠키 (만료 시간: 1시간, Value: SESSION=session-id 형태)")
                                .optional()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("소셜 회원가입 api")
                                .description("소셜 회원가입 api입니다.")
                                .responseFields(responseFieldDescriptors)
                                .requestFields(requestFieldDescriptors)
                                .build()));
    }

    @Test
    @Transactional
    void 소셜회원가입_성공() throws Exception {
        // given

        // session 수동 생성
        PendingSocialUserVo pendingSocialUserVo =
                PendingSocialUserVo.from(SocialProvider.KAKAO, "12345");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(PENDING_SOCIAL_USER_SESSION_KEY, pendingSocialUserVo);

        SocialUserSignupRequest request =
                new SocialUserSignupRequest(AgeGroup.FIFTIES, List.of(HashTag.PERFUME));

        // when
        ResultActions resultActions = getResultActions(request, session);
        MvcResult result = resultActions.andReturn();
        result.getResponse().addHeader("Set-Cookie", "SESSION=session-id; Path=/; HttpOnly");

        // then
        resultActions.andExpect(status().isCreated());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 소셜회원가입_실패1() throws Exception {
        // given
        SocialUserSignupRequest request =
                new SocialUserSignupRequest(AgeGroup.FIFTIES, List.of(HashTag.PERFUME));
        MockHttpSession fakeSession = new MockHttpSession();
        fakeSession.invalidate();

        // when
        ResultActions resultActions = getResultActions(request, fakeSession);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.INVALID_SEESION_FOR_SOCIAL_USER_SIGNUP
                                                .getHttpStatus()
                                                .value()))
                .andExpect(
                        jsonPath("code")
                                .value(
                                        ErrorResponseCode.INVALID_SEESION_FOR_SOCIAL_USER_SIGNUP
                                                .getCode()));

        // documentation
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 소셜회원가입_실패2() throws Exception {
        // given
        SocialUserCreationCommand command =
                SocialUserCreationCommand.of(
                        SocialProvider.KAKAO,
                        "12345",
                        "닉네임",
                        AgeGroup.FIFTIES,
                        List.of(HashTag.PERFUME));
        saveUserPort.saveSocialUser(User.createSocailUser(command));

        PendingSocialUserVo pendingSocialUserVo =
                PendingSocialUserVo.from(SocialProvider.KAKAO, "12345");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(PENDING_SOCIAL_USER_SESSION_KEY, pendingSocialUserVo);

        SocialUserSignupRequest request =
                new SocialUserSignupRequest(AgeGroup.FIFTIES, List.of(HashTag.PERFUME));

        // when
        ResultActions resultActions = getResultActions(request, session);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.USER_ALREADY_EXISTS.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.USER_ALREADY_EXISTS.getCode()));

        // documentation
        resultActions.andDo(getDocument(3));
    }
}
