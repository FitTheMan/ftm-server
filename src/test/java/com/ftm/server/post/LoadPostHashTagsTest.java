package com.ftm.server.post;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;

public class LoadPostHashTagsTest extends BaseTest {

    private final List<FieldDescriptor> responseFieldLoadPostHashTags =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"),
                    fieldWithPath("data.results[]").type(ARRAY).description("게시글 해시태그 목록 결과"),
                    fieldWithPath("data.results[].category")
                            .type(OBJECT)
                            .description("게시글 해시태그 카테고리"),
                    fieldWithPath("data.results[].category.name")
                            .type(STRING)
                            .description("게시글 해시태그 카테고리 이름"),
                    fieldWithPath("data.results[].category.label")
                            .type(STRING)
                            .description("게시글 해시태그 카테고리 라벨"),
                    fieldWithPath("data.results[].hashtags[]")
                            .type(ARRAY)
                            .optional()
                            .description("게시글 해시태그 목록"),
                    fieldWithPath("data.results[].hashtags[].name")
                            .type(STRING)
                            .description("게시글 해시태그 이름. 게시글 생성/수정 시 전달"),
                    fieldWithPath("data.results[].hashtags[].tag")
                            .type(STRING)
                            .description("게시글 해시태그 태그 이름"));

    private ResultActions getResultActions() throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/hashtags"));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "loadPostHashTags/" + identifier,
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldLoadPostHashTags),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저픽 게시글")
                                .summary("유저픽 게시글 해시태그 목록 조회")
                                .description(
                                        "유저픽 게시글 해시태그 목록을 조회하는 api 입니다. 게시글 생성/수정 시 해시태그의 영문 이름을 전달해주세요.")
                                .responseFields(responseFieldLoadPostHashTags)
                                .build()));
    }

    @Test
    void 게시글_해시태그_목록_조회_성공() throws Exception {
        // when
        ResultActions resultActions = getResultActions();

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // document
        resultActions.andDo(getDocument(1));
    }
}
