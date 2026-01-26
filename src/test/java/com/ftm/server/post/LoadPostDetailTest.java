package com.ftm.server.post;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostProductRequest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostRequest;
import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.application.port.in.post.SavePostUseCase;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.s3.S3PostImageUploadPort;
import com.ftm.server.application.port.out.s3.S3PostProductImageUploadPort;
import com.ftm.server.application.port.out.transcation.AfterRollbackExecutorPort;
import com.ftm.server.application.vo.post.PostInfoVo;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.PostHashtag;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class LoadPostDetailTest extends BaseTest {

    @Autowired private SavePostUseCase savePostUseCase;
    @MockitoSpyBean private S3PostImageUploadPort s3PostImageUploadPort;
    @MockitoSpyBean private S3PostProductImageUploadPort s3PostProductImageUploadPort;
    @MockitoSpyBean private S3ImageDeletePort s3ImageDeletePort;
    @MockitoSpyBean private AfterRollbackExecutorPort afterRollbackExecutorPort;

    private final ParameterDescriptor pathParametersForPostId =
            parameterWithName("postId").description("게시글 ID");

    private final List<FieldDescriptor> responseFieldLoadPostDetail =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"),
                    fieldWithPath("data.postId").type(NUMBER).description("게시글 ID"),
                    fieldWithPath("data.title").type(STRING).description("게시글 제목"),
                    fieldWithPath("data.content").type(STRING).description("게시글 내용"),
                    fieldWithPath("data.hashtags[]").type(ARRAY).description("게시글 해시태그 목록"),
                    fieldWithPath("data.viewCount").type(NUMBER).description("게시글 조회수"),
                    fieldWithPath("data.likeCount").type(NUMBER).description("게시글 좋아요 수"),
                    fieldWithPath("data.userLikeYn").type(BOOLEAN).description("사용자 게시글 좋아요 여부"),
                    fieldWithPath("data.userBookmarkYn")
                            .type(BOOLEAN)
                            .description("사용자 게시글 북마크 여부"),
                    fieldWithPath("data.createdAt").type(STRING).description("게시글 생성 날짜"),
                    fieldWithPath("data.updatedAt").type(STRING).description("게시글 수정 날짜"),
                    fieldWithPath("data.postImages[]").type(ARRAY).description("게시글 이미지 목록 정보"),
                    fieldWithPath("data.postImages[].postImageId")
                            .type(NUMBER)
                            .description("게시글 이미지 ID"),
                    fieldWithPath("data.postImages[].imageUrl")
                            .type(STRING)
                            .description("게시글 이미지 URL"),
                    fieldWithPath("data.writer").type(OBJECT).description("게시글 작성자 정보"),
                    fieldWithPath("data.writer.userId").type(NUMBER).description("게시글 작성자 ID"),
                    fieldWithPath("data.writer.nickname").type(STRING).description("게시글 작성자 닉네임"),
                    fieldWithPath("data.writer.imageUrl")
                            .type(STRING)
                            .description("게시글 작성자 프로필 이미지 URL"),
                    fieldWithPath("data.postProducts[]").type(ARRAY).description("게시글 상품 목록 정보"),
                    fieldWithPath("data.postProducts[].postProductId")
                            .type(NUMBER)
                            .description("게시글 상품 ID"),
                    fieldWithPath("data.postProducts[].name").type(STRING).description("게시글 상품 이름"),
                    fieldWithPath("data.postProducts[].brand")
                            .type(STRING)
                            .description("게시글 상품 브랜드"),
                    fieldWithPath("data.postProducts[].recommendedCount")
                            .type(NUMBER)
                            .description("게시글 상품 추천수"),
                    fieldWithPath("data.postProducts[].hashtags[]")
                            .type(ARRAY)
                            .description("게시글 상품 해시태그 목록"),
                    fieldWithPath("data.postProducts[].postProductImage")
                            .type(OBJECT)
                            .description("게시글 상품 이미지 정보"),
                    fieldWithPath("data.postProducts[].postProductImage.postProductImageId")
                            .type(NUMBER)
                            .description("게시글 상품 이미지 ID"),
                    fieldWithPath("data.postProducts[].postProductImage.imageUrl")
                            .type(STRING)
                            .description("게시글 상품 이미지 URL"));

    private Long savedPostId;

    private ResultActions getResultActions(Long postId, MockHttpSession session) throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/posts/{postId}", postId)
                        .session(session));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "loadPostDetail/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                pathParameters(pathParametersForPostId),
                responseFields(responseFieldLoadPostDetail),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저픽 게시글")
                                .summary("유저픽 게시글 상세 조회 api")
                                .description("유저픽 게시글 상세 조회 api 입니다.")
                                .responseFields(responseFieldLoadPostDetail)
                                .build()));
    }

    @BeforeEach
    @Transactional
    void setUp() throws Exception {
        User user = createTestUser("test@gmail.com", "test1234!");

        SavePostRequest postRequest =
                new SavePostRequest(
                        "독도 토너 추천",
                        List.of(PostHashtag.CLEANSING),
                        "<div>test</div>",
                        List.of(new SavePostProductRequest(-1, "독도 토너", "라운드랩", List.of())));

        // s3 실제 호출 대신 mock 대입
        doReturn(List.of()).when(s3PostImageUploadPort).uploadImages(new ArrayList<>(List.of()));
        doReturn(List.of())
                .when(s3PostProductImageUploadPort)
                .uploadImages(new ArrayList<>(List.of()));
        doNothing().when(s3ImageDeletePort).deleteImages(any());
        doNothing().when(afterRollbackExecutorPort).doAfterRollback(any());

        PostInfoVo post =
                savePostUseCase.execute(
                        SavePostCommand.from(user.getId(), postRequest, List.of(), List.of()));
        savedPostId = post.getId();
    }

    @Test
    @Transactional
    void 유저픽_게시글_상세_조회_성공() throws Exception {

        // when
        MockHttpSession session = login("test@gmail.com");
        ResultActions resultActions = getResultActions(savedPostId, session);

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // document
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 유저픽_게시글_상세_조회_성공2() throws Exception {
        // when
        ResultActions resultActions = getResultActions(savedPostId, new MockHttpSession());

        // then
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @Transactional
    void 유저픽_게시글_상세_조회_실패() throws Exception {
        // when
        ResultActions resultActions = getResultActions(1000L, new MockHttpSession());

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.POST_NOT_FOUND.getHttpStatus().value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(2));
    }
}
