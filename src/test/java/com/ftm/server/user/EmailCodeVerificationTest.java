package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.application.dto.command.EmailVerificationLogCreationCommand;
import com.ftm.server.application.service.EmailVerificationLogsService;
import com.ftm.server.web.dto.request.EmailCodeVerificationRequest;
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

public class EmailCodeVerificationTest extends BaseTest {

    @Autowired private EmailVerificationLogsService emailVerificationLogsService;

    private final List<FieldDescriptor> requestFieldDescriptors =
            List.of(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("인증 email"),
                    fieldWithPath("code").type(JsonFieldType.STRING).description("인증 코드"));

    private final List<FieldDescriptor> responseFieldDescriptors =
            List.of(
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("data"),
                    fieldWithPath("data.isVerified")
                            .type(JsonFieldType.BOOLEAN)
                            .description("검증 성공 여부"));

    private ResultActions getResultActions(EmailCodeVerificationRequest request) throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.post("/api/users/email/authentication/code")
                        .contentType(MediaType.APPLICATION_JSON) // request body content type
                        .content(mapper.writeValueAsString(request)));
    }

    // 문서화 반환 함수
    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "emailCodeVerification/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldDescriptors),
                requestFields(requestFieldDescriptors),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("이메일 인증 코드 검증 api")
                                .description("이메일 인증 코드를 검증하는 api입니다.")
                                .responseFields(responseFieldDescriptors)
                                .requestFields(requestFieldDescriptors)
                                .build()));
    }

    @Test
    @Transactional
    void 이메일_인증코드_검증_성공() throws Exception {

        String email = "test@gmail.com";
        String code = "123456";
        // given
        emailVerificationLogsService.saveEmailVerificationLogs(
                EmailVerificationLogCreationCommand.of(email, code));

        // when
        ResultActions resultActions =
                getResultActions(new EmailCodeVerificationRequest(email, code));

        // then
        resultActions.andExpect(status().isOk());

        // documentation
        resultActions.andDo(getDocument(1));
    }
}
