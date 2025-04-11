package com.ftm.server.grooming;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.application.port.out.cache.LoadGroomingTestsWithCachePort;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class LoadGroomingTestsTest extends BaseTest {

    @SuppressWarnings("removal")
    @SpyBean
    private LoadGroomingTestsWithCachePort loadGroomingTestsWithCachePort;

    private final List<FieldDescriptor> responseFieldLoadGroomingTests =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("data"),
                    fieldWithPath("data.totalCount").type(NUMBER).description("그루밍 테스트 총 문항 수"),
                    fieldWithPath("data.groomingTests")
                            .type(ARRAY)
                            .description("그루밍 테스트 목록 (순서 랜덤, 질문:답변목록 조합)"),
                    fieldWithPath("data.groomingTests[].groomingTestQuestionId")
                            .type(NUMBER)
                            .description("그루밍 테스트 질문 id"),
                    fieldWithPath("data.groomingTests[].question")
                            .type(STRING)
                            .description("그루밍 테스트 질문 내용"),
                    fieldWithPath("data.groomingTests[].groomingCategory")
                            .type(STRING)
                            .description("그루밍 테스트 질문 카테고리"),
                    fieldWithPath("data.groomingTests[].answers")
                            .type(ARRAY)
                            .description("그루밍 테스트 답변 목록"),
                    fieldWithPath("data.groomingTests[].answers[].groomingTestAnswerId")
                            .type(NUMBER)
                            .description("그루밍 테스트 답변 id"),
                    fieldWithPath("data.groomingTests[].answers[].answer")
                            .type(STRING)
                            .description("그루밍 테스트 답변 내용"));

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "loadGroomingTests/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldLoadGroomingTests),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("그루밍 테스트")
                                .summary("그루밍 테스트 목록 조회 api")
                                .description("그루밍 테스트 목록 조회 api 입니다.")
                                .responseFields(responseFieldLoadGroomingTests)
                                .build()));
    }

    @Test
    @Transactional
    void 그루밍_테스트_목록_조회_성공() throws Exception {
        // then
        // 첫번째 호출 -> 캐시 miss -> 실제 메서드 실행
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/grooming/tests"))
                .andExpect(status().isOk());

        // 두번째 호출 -> 캐시 HIT -> 메소드 호출 없이 응답
        ResultActions resultActions =
                mockMvc.perform(RestDocumentationRequestBuilders.get("/api/grooming/tests"))
                        .andExpect(status().isOk());

        verify(loadGroomingTestsWithCachePort, times(1)).loadGroomingTestsCache();

        // documentation
        resultActions.andDo(getDocument(1));
    }
}
