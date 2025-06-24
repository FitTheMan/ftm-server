package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.user.dto.request.EmailCodeVerificationRequest;
import com.ftm.server.application.command.user.EmailVerificationLogCreationCommand;
import com.ftm.server.application.port.out.persistence.user.SaveEmailVerificationLogPort;
import com.ftm.server.application.port.out.persistence.user.SaveUserPort;
import com.ftm.server.domain.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
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

    @Autowired private SaveEmailVerificationLogPort saveEmailVerificationLogPort;
    @Autowired private SaveUserPort saveUserPort;

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
                            .description("검증 성공 여부"),
                    fieldWithPath("data.isRecoverable")
                            .type(JsonFieldType.BOOLEAN)
                            .description("계정 복구 가능 여부"));

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
        saveEmailVerificationLogPort.saveEmailVerificationLogs(
                EmailVerificationLogCreationCommand.of(email, code));

        // when
        ResultActions resultActions =
                getResultActions(new EmailCodeVerificationRequest(email, code));

        // then
        resultActions.andExpect(status().isOk());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 이메일_인증코드_검증_실패() throws Exception {

        String email = "test@gmail.com";
        String correctCode = "123456";
        String wrongCode = "654321";
        
        // given
        saveEmailVerificationLogPort.saveEmailVerificationLogs(
                EmailVerificationLogCreationCommand.of(email, correctCode));

        // when
        ResultActions resultActions =
                getResultActions(new EmailCodeVerificationRequest(email, wrongCode));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isVerified").value(false))
                .andExpect(jsonPath("$.data.isRecoverable").value(false));

        // documentation
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 이메일_인증코드_검증_성공_soft_delete_사용자_복구_가능() throws Exception {

        String email = "softdeleted@gmail.com";
        String code = "123456";
        
        // given - soft delete된 사용자 생성
        User softDeletedUser = createSoftDeletedUser(email, "password123!");
        
        // given - 이메일 인증 로그 생성
        saveEmailVerificationLogPort.saveEmailVerificationLogs(
                EmailVerificationLogCreationCommand.of(email, code));

        // when
        ResultActions resultActions =
                getResultActions(new EmailCodeVerificationRequest(email, code));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isVerified").value(true))
                .andExpect(jsonPath("$.data.isRecoverable").value(true));

        // documentation
        resultActions.andDo(getDocument(3));
    }
}
