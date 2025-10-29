package com.ftm.server.post;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostProductRequest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostRequest;
import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.application.command.post.SavePostProductCommand;
import com.ftm.server.application.port.out.persistence.post.SavePostPort;
import com.ftm.server.application.port.out.persistence.post.SavePostProductPort;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.PostHashtag;
import com.ftm.server.domain.enums.ProductHashtag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class LoadProductsByHashTagTest extends BaseTest {

    @Autowired private SavePostPort savePostPort;

    @Autowired private SavePostProductPort savePostProductPort;
    private final List<FieldDescriptor> responseFields =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data")
                            .type(ARRAY)
                            .optional()
                            .description("응답 데이터 : 대상 상품이 없는 경우 빈 배열"),
                    fieldWithPath("data[].productId").type(NUMBER).description("상품 id"),
                    fieldWithPath("data[].productName").type(STRING).description("상품 이름"),
                    fieldWithPath("data[].productImage").type(STRING).description("상품 이미지 url"),
                    fieldWithPath("data[].likeYn").type(BOOLEAN).description("사용자 추천 버튼 누름 여부"),
                    fieldWithPath("data[].brand").type(STRING).description("브랜드"),
                    fieldWithPath("data[].recommendedCount").type(NUMBER).description("추천수"),
                    fieldWithPath("data[].postId").type(NUMBER).description("상품이 속한 게시글 id"));

    private final List<FieldDescriptor> requestFields =
            List.of(
                    fieldWithPath("hashTagList")
                            .type(ARRAY)
                            .optional()
                            .description(
                                    "사용자가 선택한 해시태그 목록. 빈 배열이 전달되거나 field 전체가 전달되지 않을 경우, 전체 상품을 최신순으로 반환함.")
                            .attributes(
                                    new Attributes.Attribute(
                                            "constraint",
                                            "게시글 상품 해시태그 목록 조회 api response 중 result.$.hashtags.name 값을 전달")));

    private ResultActions getResultActions() throws Exception {
        Map<String, List<String>> req = new HashMap<>();
        req.put("hashTagList", List.of(ProductHashtag.HAND_CARE.name()));
        return mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/posts/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "loadPostProducts/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                requestFields(requestFields),
                responseFields(responseFields),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("해시태그 추천")
                                .summary("해시태그 추천 - 해시태그 기반 상품 조회 api")
                                .description("해시태그 기반 상품 조회 api 입니다")
                                .requestFields(requestFields)
                                .responseFields(responseFields)
                                .build()));
    }

    @Test
    @Transactional
    @DisplayName("테스트 성공")
    public void test1() throws Exception {
        // given
        BaseTest.SessionAndUser sessionAndUser = createUserAndLoginAndReturnUser(); // 로그인 처리

        User user = sessionAndUser.user();

        // test 용 post 생성
        Post post =
                savePostPort.savePost(
                        Post.create(
                                SavePostCommand.from(
                                        user.getId(),
                                        new SavePostRequest(
                                                "test1",
                                                List.of(
                                                        PostHashtag.SUN_CARE,
                                                        PostHashtag.CLEANSING),
                                                "content1",
                                                new ArrayList<>()),
                                        new ArrayList<>(),
                                        new ArrayList<>())));

        savePostProductPort.savePostProducts(
                List.of(
                        PostProduct.create(
                                SavePostProductCommand.from(
                                                new SavePostProductRequest(
                                                        -1,
                                                        "상품 3번",
                                                        "이니스프리",
                                                        List.of(
                                                                ProductHashtag.HAND_CARE,
                                                                ProductHashtag.INCENSE)))
                                        .withPostId(post.getId()))));

        // when
        ResultActions resultActions = getResultActions();

        // then
        resultActions
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data", hasSize(1)));

        // documentation
        resultActions.andDo(getDocument(1));
    }
}
