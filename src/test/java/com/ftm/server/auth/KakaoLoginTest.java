package com.ftm.server.auth;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
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
import com.ftm.server.application.dto.command.KakaoAuthCommand;
import com.ftm.server.application.port.SocialAuthClientPort;
import com.ftm.server.application.port.repository.UserImageRepository;
import com.ftm.server.application.port.repository.UserRepository;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import com.ftm.server.infrastructure.oauth.kakao.KakaoAuthUser;
import com.ftm.server.web.dto.request.KakaoLoginRequest;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class KakaoLoginTest extends BaseTest {

    @MockitoBean private SocialAuthClientPort<KakaoAuthCommand, KakaoAuthUser> kakaoClient;

    @Autowired private UserRepository userRepository;
    @Autowired private UserImageRepository userImageRepository;

    private final List<FieldDescriptor> requestFieldKakaoLogin =
            List.of(fieldWithPath("authorizationCode").type(STRING).description("카카오 인증 코드"));

    private final List<FieldDescriptor> responseFieldKakaoLogin =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("data"),
                    fieldWithPath("data.id").type(NUMBER).description("유저 ID"),
                    fieldWithPath("data.nickname").type(STRING).description("유저 닉네임"),
                    fieldWithPath("data.socialProvider").type(STRING).description("소셜 제공자"),
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

    private ResultActions getResultActions(KakaoLoginRequest request) throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/auth/login/kakao")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(request)));
    }

    private RestDocumentationResultHandler getDocument(
            Integer identifier, String sessionDescription) {
        return document(
                "kakaoLogin/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                requestFields(requestFieldKakaoLogin),
                responseHeaders(
                        headerWithName("Set-Cookie").description(sessionDescription).optional()),
                responseFields(responseFieldKakaoLogin),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증/인가")
                                .summary("카카오 로그인 api")
                                .description("카카오 로그인 api 입니다.")
                                .responseFields(responseFieldKakaoLogin)
                                .build()));
    }

    @Test
    @Transactional
    void 카카오_로그인_성공1() throws Exception {
        // given
        User testUser = userRepository.save(User.createTestKakaoUser());
        UserImage testUserImage = UserImage.createUserImage(testUser);
        userImageRepository.save(testUserImage);

        KakaoLoginRequest request = new KakaoLoginRequest("test_code");
        KakaoAuthUser testKakaoUser = KakaoAuthUser.from("test_kakao_id");
        given(kakaoClient.authenticate(any(KakaoAuthCommand.class))).willReturn(testKakaoUser);

        // when
        ResultActions resultActions = getResultActions(request);
        MvcResult result = resultActions.andReturn();

        // 세션 쿠키 수동 추가 (문서화 통과용)
        result.getResponse().addHeader("Set-Cookie", "SESSION=session-id; Path=/; HttpOnly");

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", Matchers.containsString("SESSION")))
                .andDo(print());

        // documentation
        resultActions.andDo(
                getDocument(1, "로그인 성공 시 발급되는 세션 쿠키 (만료 시간: 1시간, Value: SESSION=session-id 형태)"));
    }

    @Test
    @Transactional
    void 카카오_로그인_성공2() throws Exception {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("test_code");
        KakaoAuthUser testKakaoUser = KakaoAuthUser.from("test_kakao_id");
        given(kakaoClient.authenticate(any(KakaoAuthCommand.class))).willReturn(testKakaoUser);

        // when
        ResultActions resultActions = getResultActions(request);
        MvcResult result = resultActions.andReturn();

        // 세션 쿠키 수동 추가 (문서화 통과용)
        result.getResponse().addHeader("Set-Cookie", "SESSION=session-id; Path=/; HttpOnly");

        // then
        resultActions
                .andExpect(status().isAccepted())
                .andExpect(header().string("Set-Cookie", Matchers.containsString("SESSION")))
                .andDo(print());

        // documentation
        resultActions.andDo(
                getDocument(
                        2, "소셜 유저 정보를 담은 임시 세션 쿠키 (만료 시간: 5분, , Value: SESSION=session-id 형태)"));
    }

    @Test
    @Transactional
    void 카카오_로그인_실패1() throws Exception {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("test_code");
        doThrow(new CustomException(ErrorResponseCode.KAKAO_AUTH_TOKEN_EXCHANGE_FAILED))
                .when(kakaoClient)
                .authenticate(any(KakaoAuthCommand.class));

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.KAKAO_AUTH_TOKEN_EXCHANGE_FAILED
                                                .getHttpStatus()
                                                .value()))
                .andExpect(
                        jsonPath("code")
                                .value(
                                        ErrorResponseCode.KAKAO_AUTH_TOKEN_EXCHANGE_FAILED
                                                .getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(3, ""));
    }

    @Test
    @Transactional
    void 카카오_로그인_실패2() throws Exception {
        // given
        KakaoLoginRequest request = new KakaoLoginRequest("test_code");
        doThrow(new CustomException(ErrorResponseCode.KAKAO_USER_PROFILE_FETCH_FAILED))
                .when(kakaoClient)
                .authenticate(any(KakaoAuthCommand.class));

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.KAKAO_USER_PROFILE_FETCH_FAILED
                                                .getHttpStatus()
                                                .value()))
                .andExpect(
                        jsonPath("code")
                                .value(ErrorResponseCode.KAKAO_USER_PROFILE_FETCH_FAILED.getCode()))
                .andDo(print());

        // documentation
        resultActions.andDo(getDocument(4, ""));
    }
}
