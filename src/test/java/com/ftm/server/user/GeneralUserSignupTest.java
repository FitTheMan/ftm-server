package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.application.dto.command.EmailVerificationLogCreationCommand;
import com.ftm.server.application.dto.command.GeneralUserCreationCommand;
import com.ftm.server.application.port.repository.EmailVerificationLogsRepository;
import com.ftm.server.application.service.UserService;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.EmailVerificationLogs;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import com.ftm.server.web.dto.request.GeneralUserSignupRequest;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class GeneralUserSignupTest extends BaseTest {

    @Autowired private EmailVerificationLogsRepository emailVerificationLogsRepository;

    @Autowired private UserService userService;

    private final List<FieldDescriptor> requestFieldDescriptors =
            List.of(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("회원가입 email"),
                    fieldWithPath("password")
                            .type(JsonFieldType.STRING)
                            .description("회원가입 password"),
                    fieldWithPath("age")
                            .type(JsonFieldType.STRING)
                            .description("연령대. 사용자 정보 옵션 조회 api에서 반환받은 value 값을 전달해 주세요."),
                    fieldWithPath("hashtags")
                            .type(JsonFieldType.ARRAY)
                            .description("관심 해시태그. 사용자 정보 옵션 조회 api에서 반환받은 value 값을 전달해 주세요."));

    private final List<FieldDescriptor> responseFieldDescriptors =
            List.of(
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                    fieldWithPath("data")
                            .type(JsonFieldType.OBJECT)
                            .optional()
                            .description("data"));

    private ResultActions getResultActions(GeneralUserSignupRequest request) throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON) // request body content type
                        .content(mapper.writeValueAsString(request)));
    }

    // 문서화 반환 함수
    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "generalUserSignUp/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldDescriptors),
                requestFields(requestFieldDescriptors),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("일반 회원가입 api")
                                .description("일반 회원가입 api입니다.")
                                .responseFields(responseFieldDescriptors)
                                .requestFields(requestFieldDescriptors)
                                .build()));
    }

    @Test
    @Transactional
    void 일반회원가입_성공() throws Exception {
        // given
        String email = "test@gmail.com";
        String code = "123456";

        EmailVerificationLogCreationCommand command =
                new EmailVerificationLogCreationCommand(email, code);
        EmailVerificationLogs data = EmailVerificationLogs.from(command);
        data.updateVerificationStatus(true);
        emailVerificationLogsRepository.save(data);

        GeneralUserSignupRequest request =
                new GeneralUserSignupRequest(
                        email, "123456", AgeGroup.FIFTIES, List.of(HashTag.PERFUME));

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions.andExpect(status().isCreated());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 일반회원가입_실패1() throws Exception {
        // given
        GeneralUserSignupRequest request =
                new GeneralUserSignupRequest(
                        "test@gmail.com", "123456", AgeGroup.FIFTIES, List.of(HashTag.PERFUME));

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.EMAIL_NOT_VERIFIED.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.EMAIL_NOT_VERIFIED.getCode()));

        // documentation
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 일반회원가입_실패2() throws Exception {
        // given
        String email = "test@gmail.com";
        HashTag[] hashTags = {HashTag.PERFUME};
        GeneralUserCreationCommand command =
                new GeneralUserCreationCommand(email, "123456", "닉넴", AgeGroup.FIFTIES, hashTags);
        userService.createGeneralUser(command);

        GeneralUserSignupRequest request =
                new GeneralUserSignupRequest(
                        "test@gmail.com", "123456", AgeGroup.FIFTIES, List.of(HashTag.PERFUME));

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.USER_ALREADY_EXISTS.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.USER_ALREADY_EXISTS.getCode()));

        // documentation
        resultActions.andDo(getDocument(3));
    }
}
