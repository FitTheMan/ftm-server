package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.ResultActions;

public class EmailDuplicationCheckTest extends BaseTest {

    private final List<ParameterDescriptor> queryParametersForEmail =
            List.of(
                    parameterWithName("email")
                            .description("중복 확인 email")
                            .attributes(new Attributes.Attribute("constraint", "email 형식")));

    private final List<FieldDescriptor> responseFieldDescriptorsForEmailDuplicationCheck =
            List.of(
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("data"),
                    fieldWithPath("data.isDuplicated").type("Boolean").description("중복 여부"));

    private ResultActions getResultActions(String email) throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.get("/api/users/email/duplication")
                        .param("email", email) // query parameter
                );
    }

    // 문서화 반환 함수
    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "api/users/email/duplication/" + identifier,
                preprocessRequest(prettyPrint()), // request 출력 형식 지정->host 이름 변경
                preprocessResponse(prettyPrint(), getModifiedHeader()), // response 출력 형식 지정
                responseFields(
                        responseFieldDescriptorsForEmailDuplicationCheck), // response body field
                // descriptor
                queryParameters(queryParametersForEmail), // query parameter descriptor
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("이메일 중복 확인 api")
                                .description("email 중복 확인 api입니다.")
                                .build()));
    }

    @Test
    @Transactional
    void 이메일_중복_확인_성공1() throws Exception {
        // given

        // when
        ResultActions resultActions = getResultActions("test@gmail.com");

        // then
        resultActions.andExpect(status().isOk());

        // documentation
        resultActions.andDo(getDocument(1));
    }
}
