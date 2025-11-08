package com.ftm.server.post;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.PostHashtag;
import com.ftm.server.domain.enums.ProductHashtag;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class CreateProductLikeTest extends BaseTest {

    @Autowired private SavePostPort savePostPort;

    @Autowired private SavePostProductPort savePostProductPort;

    private final ParameterDescriptor pathParameters =
            parameterWithName("productId").description("상품 ID");

    private final List<FieldDescriptor> responseFields =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"),
                    fieldWithPath("data.isCreated")
                            .type(BOOLEAN)
                            .description("좋아요 생성 여부. true면 좋아요 생성, false 면 좋아요 취소"));

    private ResultActions getResultActions(Long productId, MockHttpSession session)
            throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/products/{productId}/like", productId)
                        .session(session));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "createProductLike/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                pathParameters(pathParameters),
                responseFields(responseFields),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저픽 게시글")
                                .summary("해시태그 상품 추천 - 상품 좋아요 api")
                                .description("해시태그 상품 추천 - 상품 좋아요 api 입니다.")
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

        MockHttpSession session = login("test@gmail.com");

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

        List<PostProduct> postProduct =
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
        ResultActions resultActions = getResultActions(postProduct.get(0).getId(), session);

        // then
        resultActions
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.isCreated").value(true));

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    @DisplayName("테스트 실패")
    public void test2() throws Exception {
        // given
        BaseTest.SessionAndUser sessionAndUser = createUserAndLoginAndReturnUser(); // 로그인 처리

        MockHttpSession session = login("test@gmail.com");

        // when
        ResultActions resultActions = getResultActions(1000L, session);

        // then
        resultActions
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(
                        jsonPath("$.code")
                                .value(ErrorResponseCode.POST_PRODUCT_NOT_FOUND.getCode()));

        // documentation
        resultActions.andDo(getDocument(2));
    }
}
