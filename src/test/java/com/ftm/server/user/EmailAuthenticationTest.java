package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.dto.request.EmailAuthenticationRequest;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.dto.command.EmailAuthenticationCommand;
import com.ftm.server.domain.usecase.user.EmailAuthenticationUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.ResultActions;

public class EmailAuthenticationTest extends BaseTest {

    @Autowired private EmailAuthenticationUseCase emailAuthenticationUseCase;

    @MockitoSpyBean private EmailAuthenticationUseCase mockitoEmailAuthenticationUseCase;

    private final List<FieldDescriptor> responseFieldDescriptors =
            List.of(
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                    fieldWithPath("data")
                            .type(JsonFieldType.OBJECT)
                            .optional()
                            .description("data")
                            .attributes(new Attributes.Attribute("nullable", "반환할 data 없음. Null")));

    private final List<FieldDescriptor> requestFieldDescriptors =
            List.of(
                    fieldWithPath("email")
                            .type(JsonFieldType.STRING)
                            .description("인증 email")
                            .attributes(new Attributes.Attribute("constraint", "이메일 형식")));

    private ResultActions getResultActions(EmailAuthenticationRequest request) throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.post("/api/users/email/authentication")
                        .contentType(MediaType.APPLICATION_JSON) // request body content type
                        .content(mapper.writeValueAsString(request)));
    }

    // 문서화 반환 함수
    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "emailAuthentication/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldDescriptors),
                requestFields(requestFieldDescriptors),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("이메일 인증 api")
                                .description("email 인증용 코드를 발송하는 api입니다.")
                                .responseFields(responseFieldDescriptors)
                                .requestFields(requestFieldDescriptors)
                                .build()));
    }

    @Transactional
    @Test
    void 이메일_인증코드_전송_성공() throws Exception {
        // given
        EmailAuthenticationRequest request = new EmailAuthenticationRequest("test@gmail.com");

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions.andExpect(status().isOk());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Transactional
    @Test
    void 이메일_인증코드_전송_실패1() throws Exception {
        // given
        EmailAuthenticationRequest request = new EmailAuthenticationRequest("test@gmail.com");
        for (int i = 0; i < 5; i++) {
            emailAuthenticationUseCase.sendEmailAuthenticationCode(
                    EmailAuthenticationCommand.from(request));
        }
        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.EXCEED_NUMBER_OF_TRIAL
                                                .getHttpStatus()
                                                .value()))
                .andExpect(
                        jsonPath("code").value(ErrorResponseCode.EXCEED_NUMBER_OF_TRIAL.getCode()));

        // documentation
        resultActions.andDo(getDocument(2));
    }

    @Transactional
    @Test
    void 이메일_인증코드_전송_실패2() throws Exception {
        // given
        EmailAuthenticationRequest request = new EmailAuthenticationRequest("test@gmail.com");
        doThrow(new CustomException(ErrorResponseCode.FAIL_TO_SEND_EMAIL))
                .when(mockitoEmailAuthenticationUseCase)
                .sendEmailAuthenticationCode(EmailAuthenticationCommand.from(request));

        // when
        ResultActions resultActions = getResultActions(request);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.FAIL_TO_SEND_EMAIL.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.FAIL_TO_SEND_EMAIL.getCode()));

        // documentation
        resultActions.andDo(getDocument(3));
    }
}
