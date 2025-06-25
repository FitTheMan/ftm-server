package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.user.dto.request.RecoverUserAccountRequest;
import com.ftm.server.adapter.out.persistence.mapper.EmailVerificationLogsMapper;
import com.ftm.server.adapter.out.persistence.repository.EmailVerificationLogsRepository;
import com.ftm.server.application.command.user.EmailVerificationLogCreationCommand;
import com.ftm.server.application.port.out.persistence.user.UpdateUserPort;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.EmailVerificationLogs;
import com.ftm.server.domain.entity.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class RecoverUserAccountTest extends BaseTest {

    @Autowired private UpdateUserPort updateUserPort;
    @Autowired private EmailVerificationLogsRepository emailVerificationLogsRepository;
    @Autowired private EmailVerificationLogsMapper emailVerificationLogsMapper;

    private final List<FieldDescriptor> requestFieldDescriptors =
            List.of(
                    fieldWithPath("email")
                            .type(STRING)
                            .description("복구할 계정의 email")
                            .attributes(new Attributes.Attribute("constraint", "email 형식")));

    private final List<FieldDescriptor> responseFieldDescriptors =
            List.of(
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("data"),
                    fieldWithPath("data.userId")
                            .type(JsonFieldType.NUMBER)
                            .description("사용자 고유 id"));

    private ResultActions getResultActions(RecoverUserAccountRequest request) throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.post("/api/users/me/recover")
                        .contentType(MediaType.APPLICATION_JSON) // request body content type
                        .content(mapper.writeValueAsString(request)));
    }

    // 문서화 반환 함수
    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "recoverUserAccount/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldDescriptors),
                requestFields(requestFieldDescriptors),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("회원 계정 복구 api")
                                .description("회원 계정 복구 api입니다.")
                                .responseFields(responseFieldDescriptors)
                                .requestFields(requestFieldDescriptors)
                                .build()));
    }

    @Test
    @DisplayName("회원 계정 복구 성공")
    @Transactional
    void test1() throws Exception {
        // given
        String email = "test@example.com";
        RecoverUserAccountRequest request = new RecoverUserAccountRequest(email);

        // 1. 이메일 검증 로그 생성 및 검증 완료 처리
        EmailVerificationLogs emailVerificationLogs =
                EmailVerificationLogs.from(EmailVerificationLogCreationCommand.of(email, "123456"));
        emailVerificationLogs.updateVerificationStatus(true);
        emailVerificationLogsRepository.save(
                emailVerificationLogsMapper.toJpaEntity(emailVerificationLogs));

        // 2. 사용자 생성 후 soft delete
        User user = createTestUser(email, "password123!");
        user.updateIsDeleted(true);
        updateUserPort.updateUser(user);

        // when & then
        getResultActions(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").exists())
                .andDo(getDocument(1));
    }

    @Test
    @DisplayName("회원 계정 복구 실패 - 복구 불가능한 상태")
    @Transactional
    void test2() throws Exception {
        // given
        String email = "invalid@example.com";
        RecoverUserAccountRequest request = new RecoverUserAccountRequest(email);

        User user = createTestUser(email, "password123!");

        // when & then
        getResultActions(request)
                .andExpect(
                        status().is(
                                        ErrorResponseCode.RECOVERING_IS_NOT_AVAILABLE
                                                .getHttpStatus()
                                                .value()))
                .andExpect(
                        jsonPath("$.code")
                                .value(ErrorResponseCode.RECOVERING_IS_NOT_AVAILABLE.getCode()))
                .andDo(getDocument(2));
    }
}
