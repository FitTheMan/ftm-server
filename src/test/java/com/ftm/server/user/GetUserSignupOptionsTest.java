package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
import org.springframework.test.web.servlet.ResultActions;

public class GetUserSignupOptionsTest extends BaseTest {

    private final List<FieldDescriptor> responseFieldDescriptors =
            List.of(
                    fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("data"),
                    fieldWithPath("data.ages").type(JsonFieldType.ARRAY).description("연령대 목록"),
                    fieldWithPath("data.ages[].value")
                            .type(JsonFieldType.STRING)
                            .description("연령대 고유 값. 회원가입시 전달"),
                    fieldWithPath("data.ages[].description")
                            .type(JsonFieldType.STRING)
                            .description("연령대 고유 값 한글 설명"),
                    fieldWithPath("data.hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
                    fieldWithPath("data.hashtags[].value")
                            .type(JsonFieldType.STRING)
                            .description("해시태그 고유 값. 회원가입시 전달"),
                    fieldWithPath("data.hashtags[].description")
                            .type(JsonFieldType.STRING)
                            .description("해시태그 고유 값 한글 설명"));

    private ResultActions getResultActions() throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.get("/api/users/options"));
    }

    // 문서화 반환 함수
    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "userSignupOptions/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldDescriptors),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("회원가입시 필요한 정보 목록 조회 api")
                                .description("회원가입시 사용자에게 받는 연령대 정보와 관심 해시태그 정보 옵션들을 조회합니다.")
                                .responseFields(responseFieldDescriptors)
                                .build()));
    }

    @Test
    @Transactional
    void 사용자_정보_옵션조회_성공() throws Exception {
        // given

        // when
        ResultActions resultActions = getResultActions();

        // then
        resultActions.andExpect(status().isOk());

        // documentation
        resultActions.andDo(getDocument(1));
    }
}
