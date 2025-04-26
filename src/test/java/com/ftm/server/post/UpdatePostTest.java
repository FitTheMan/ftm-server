package com.ftm.server.post;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostProductRequest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostRequest;
import com.ftm.server.adapter.in.web.post.dto.request.UpdatePostProductRequest;
import com.ftm.server.adapter.in.web.post.dto.request.UpdatePostRequest;
import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.application.port.in.post.SavePostUseCase;
import com.ftm.server.application.port.out.persistence.post.*;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.s3.S3PostImageUploadPort;
import com.ftm.server.application.port.out.s3.S3PostProductImageUploadPort;
import com.ftm.server.application.port.out.transcation.AfterCommitExecutorPort;
import com.ftm.server.application.port.out.transcation.AfterRollbackExecutorPort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindByPostIdQuery;
import com.ftm.server.application.vo.post.PostInfoVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.PostProductImage;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.GroomingCategory;
import com.ftm.server.domain.enums.HashTag;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

public class UpdatePostTest extends BaseTest {

    @Autowired private UpdatePostProductImagePort updatePostProductImagePort;
    @Autowired private LoadPostProductImagePort loadPostProductImagePort;
    @Autowired private LoadPostProductPort loadPostProductPort;
    @Autowired private SavePostUseCase savePostUseCase;
    @MockitoSpyBean private S3PostImageUploadPort s3PostImageUploadPort;
    @MockitoSpyBean private S3PostProductImageUploadPort s3PostProductImageUploadPort;
    @MockitoSpyBean private S3ImageDeletePort s3ImageDeletePort;
    @MockitoSpyBean private AfterCommitExecutorPort afterCommitExecutorPort;
    @MockitoSpyBean private AfterRollbackExecutorPort afterRollbackExecutorPort;

    private final ParameterDescriptor pathParametersForPostId =
            parameterWithName("postId").description("게시글 ID");

    private final List<RequestPartDescriptor> requestPartUpdatePost =
            List.of(
                    partWithName("data")
                            .description("게시글 저장 정보")
                            .attributes(
                                    new Attributes.Attribute("content-type", "application/json")),
                    partWithName("postImageFiles")
                            .optional()
                            .description("새로 추가할 게시글 이미지 파일 리스트 (List<MultipartFile> 형태, 순서를 보장)")
                            .attributes(new Attributes.Attribute("content-type", "image/*")),
                    partWithName("productImageFiles")
                            .optional()
                            .description("새로 추가할 상품 이미지 파일 리스트 (List<MultipartFile> 형태, 순서를 보장)")
                            .attributes(new Attributes.Attribute("content-type", "image/*")));

    private final List<FieldDescriptor> requestPartFieldUpdatePost =
            List.of(
                    fieldWithPath("title").type(STRING).optional().description("게시글 제목"),
                    fieldWithPath("groomingCategory")
                            .type(STRING)
                            .optional()
                            .description("게시글 그루밍 분야"),
                    fieldWithPath("hashTags[]").type(ARRAY).optional().description("게시글 해시태그 목록"),
                    fieldWithPath("content").type(STRING).optional().description("게시글 내용"),
                    fieldWithPath("deletePostImageIds[]")
                            .type(ARRAY)
                            .optional()
                            .description("삭제할 게시글 이미지 ID 목록"),
                    fieldWithPath("deleteProductIds[]")
                            .type(ARRAY)
                            .optional()
                            .description("삭제할 상품 ID 목록"),
                    fieldWithPath("addProducts[]")
                            .type(ARRAY)
                            .optional()
                            .description("새로 추가할 상품 목록"),
                    fieldWithPath("addProducts[].imageIndex")
                            .type(NUMBER)
                            .description(
                                    "상품 이미지와 매핑될 인덱스 (**1**부터 시작, 이미지를 등록하지 않은 상품이라면 **-1** 저장) ** 순서 중요 **"),
                    fieldWithPath("addProducts[].name").type(STRING).description("상품 이름"),
                    fieldWithPath("addProducts[].brand").type(STRING).description("상품 브랜드"),
                    fieldWithPath("addProducts[].hashtags[]")
                            .type(ARRAY)
                            .optional()
                            .description("상품 해시태그 목록"),
                    fieldWithPath("updateProducts[]")
                            .type(ARRAY)
                            .optional()
                            .description("수정할 상품 목록"),
                    fieldWithPath("updateProducts[].id").type(NUMBER).description("수정할 상품 ID"),
                    fieldWithPath("updateProducts[].name")
                            .type(STRING)
                            .optional()
                            .description("수정된 상품 이름"),
                    fieldWithPath("updateProducts[].brand")
                            .type(STRING)
                            .optional()
                            .description("수정된 상품 브랜드"),
                    fieldWithPath("updateProducts[].hashTags[]")
                            .type(ARRAY)
                            .optional()
                            .description("수정된 상품 해시태그 목록"),
                    fieldWithPath("updateProducts[].deleteProductImageId")
                            .type(NUMBER)
                            .optional()
                            .description("삭제할 상품 이미지 ID -> **기존 이미지를 삭제하거나, 이미지를 변경할 경우 필수**"),
                    fieldWithPath("updateProducts[].imageIndex")
                            .type(NUMBER)
                            .description(
                                    "상품 이미지와 매핑될 인덱스 (**1**부터 시작, 이미지를 등록하지 않은 상품이라면 **-1** 저장) ** 순서 중요 **"));

    private final List<FieldDescriptor> responseFieldUpdatePost =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"));

    private Long savedPostId;
    private List<Long> savedPostProductIds = List.of();

    private ResultActions getResultActions(
            MockHttpSession session,
            Long postId,
            MockMultipartFile data,
            List<MockMultipartFile> postImageFiles,
            List<MockMultipartFile> productImageFiles)
            throws Exception {
        MockMultipartHttpServletRequestBuilder builder =
                (MockMultipartHttpServletRequestBuilder)
                        RestDocumentationRequestBuilders.multipart("/api/posts/{postId}", postId)
                                .file(data) // JSON part ("data")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .session(session)
                                .with(
                                        request -> {
                                            request.setMethod("PATCH");
                                            return request;
                                        });

        // 1. List<MockMultipartFile> 처리 (게시글 이미지 리스트)
        if (postImageFiles != null && !postImageFiles.isEmpty()) {
            for (MockMultipartFile file : postImageFiles) {
                builder.file(file);
            }
        }

        // 2. Map<String, MockMultipartFile> 처리 (상품 이미지 맵)
        if (productImageFiles != null && !productImageFiles.isEmpty()) {
            for (MockMultipartFile file : productImageFiles) {
                builder.file(file);
            }
        }

        return mockMvc.perform(builder);
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "updatePost/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                pathParameters(pathParametersForPostId),
                requestParts(requestPartUpdatePost),
                requestPartFields("data", requestPartFieldUpdatePost),
                responseFields(responseFieldUpdatePost),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저픽 게시글")
                                .summary("유저픽 게시글 수정 api")
                                .description("유저픽 게시글 수정 api 입니다.")
                                .responseFields(responseFieldUpdatePost)
                                .build()));
    }

    @BeforeEach
    void setUp() throws Exception {
        User user = createTestUser("test@gmail.com", "test1234!");

        SavePostRequest postRequest =
                new SavePostRequest(
                        "독도 토너 추천",
                        GroomingCategory.BEAUTY,
                        List.of(HashTag.PERFUME),
                        "<div>test</div>",
                        List.of(
                                new SavePostProductRequest(-1, "독도 토너", "라운드랩", List.of()),
                                new SavePostProductRequest(-1, "시카 크림", "더하르나이", List.of())));

        // s3 실제 호출 대신 mock 대입
        doReturn(List.of()).when(s3PostImageUploadPort).uploadImages(new ArrayList<>(List.of()));
        doReturn(List.of())
                .when(s3PostProductImageUploadPort)
                .uploadImages(new ArrayList<>(List.of()));
        doNothing().when(s3ImageDeletePort).deleteImages(any());
        doNothing().when(afterCommitExecutorPort).doAfterCommit(any());
        doNothing().when(afterRollbackExecutorPort).doAfterRollback(any());

        PostInfoVo post =
                savePostUseCase.execute(
                        SavePostCommand.from(user.getId(), postRequest, List.of(), List.of()));
        savedPostId = post.getId();

        List<PostProduct> products =
                loadPostProductPort.loadPostProductsByPostId(FindByPostIdQuery.of(post.getId()));
        savedPostProductIds = products.stream().map(PostProduct::getId).toList();
    }

    private MockMultipartFile getExampleData(UpdatePostRequest request) throws Exception {
        return new MockMultipartFile(
                "data",
                "data.json",
                APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @Transactional
    void 게시글_수정_성공() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        Long deletePostProductId = savedPostProductIds.get(0);
        Long updatedPostProductId = savedPostProductIds.get(1);
        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(deletePostProductId),
                        List.of(new SavePostProductRequest(1, "새로운 상품 추가", "새로운 상품 추가", List.of())),
                        List.of(
                                UpdatePostProductRequest.of(
                                        updatedPostProductId,
                                        "상품 이름 수정",
                                        "상품 브랜드 수정",
                                        List.of(),
                                        null,
                                        2)));

        MockMultipartFile data = getExampleData(postRequest);
        List<MockMultipartFile> postImageFiles =
                List.of(
                        new MockMultipartFile(
                                "postImageFiles",
                                "test_01.jpg",
                                IMAGE_JPEG_VALUE,
                                "test_01".getBytes()));
        List<MockMultipartFile> productImageFiles =
                List.of(
                        new MockMultipartFile(
                                "productImageFiles",
                                "test_01.jpg",
                                IMAGE_JPEG_VALUE,
                                "test_01".getBytes()),
                        new MockMultipartFile(
                                "productImageFiles",
                                "test_02.jpg",
                                IMAGE_JPEG_VALUE,
                                "test_02".getBytes()));

        // s3 실제 호출 대신 mock 대입
        doReturn(List.of("posts/test_01.jpg"))
                .when(s3PostImageUploadPort)
                .uploadImages(new ArrayList<>(postImageFiles));
        doReturn(List.of("products/test_01.jpg", "products/test_02.jpg"))
                .when(s3PostProductImageUploadPort)
                .uploadImages(new ArrayList<>(productImageFiles));

        // when
        ResultActions resultActions =
                getResultActions(session, savedPostId, data, postImageFiles, productImageFiles);

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // document
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 게시글_수정_실패1() throws Exception {
        // given
        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of());
        MockMultipartFile data = getExampleData(postRequest);

        // when
        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.multipart(
                                        "/api/posts/{postId}", savedPostId)
                                .file(data) // JSON part ("data")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .with(
                                        request -> {
                                            request.setMethod("PATCH");
                                            return request;
                                        }));

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHENTICATED.getHttpStatus().value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 게시글_수정_실패2() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of());
        MockMultipartFile data = getExampleData(postRequest);

        // when
        ResultActions resultActions = getResultActions(session, 1000L, data, List.of(), List.of());

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.POST_NOT_FOUND.getHttpStatus().value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(3));
    }

    @Test
    @Transactional
    void 게시글_수정_실패3() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin("test1@gmail.com", "asdfgcx!!");

        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of());
        MockMultipartFile data = getExampleData(postRequest);

        // when
        ResultActions resultActions =
                getResultActions(session, savedPostId, data, List.of(), List.of());

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.UNAUTHORIZED_POST_ACCESS
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(4));
    }

    @Test
    @Transactional
    void 게시글_수정_실패4() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        Long updatedPostProductId = savedPostProductIds.get(1);
        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(),
                        List.of(new SavePostProductRequest(1, "새로운 상품 추가", "새로운 상품 추가", List.of())),
                        List.of(
                                UpdatePostProductRequest.of(
                                        updatedPostProductId, null, null, List.of(), null, 2)));

        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "data.json",
                        APPLICATION_JSON_VALUE,
                        mapper.writeValueAsString(postRequest).getBytes(StandardCharsets.UTF_8));

        List<MockMultipartFile> postImageFiles = List.of();
        List<MockMultipartFile> productImageFiles =
                List.of(
                        new MockMultipartFile(
                                "productImageFiles",
                                "test_01.jpg",
                                IMAGE_JPEG_VALUE,
                                "test_01".getBytes()));

        // when
        ResultActions resultActions =
                getResultActions(session, savedPostId, data, postImageFiles, productImageFiles);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.INVALID_POST_PRODUCT_IMAGE_MAPPING
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(5));
    }

    @Test
    @Transactional
    void 게시글_수정_실패5() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        Long updatedPostProductId = savedPostProductIds.get(1);
        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(),
                        List.of(new SavePostProductRequest(1, "새로운 상품 추가", "새로운 상품 추가", List.of())),
                        List.of(
                                UpdatePostProductRequest.of(
                                        updatedPostProductId, null, null, List.of(), null, 2)));

        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "data.json",
                        APPLICATION_JSON_VALUE,
                        mapper.writeValueAsString(postRequest).getBytes(StandardCharsets.UTF_8));

        List<MockMultipartFile> postImageFiles =
                List.of(
                        new MockMultipartFile(
                                "postImageFiles",
                                "test_01.txt",
                                TEXT_PLAIN_VALUE,
                                "test_01".getBytes()));
        List<MockMultipartFile> productImageFiles = List.of();

        // when
        ResultActions resultActions =
                getResultActions(session, savedPostId, data, postImageFiles, productImageFiles);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.INVALID_IMAGE_FORMAT.getHttpStatus().value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(6));
    }

    @Test
    @Transactional
    void 게시글_수정_실패6() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        Long updatedPostProductId = savedPostProductIds.get(1);
        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(),
                        List.of(new SavePostProductRequest(1, "새로운 상품 추가", "새로운 상품 추가", List.of())),
                        List.of(
                                UpdatePostProductRequest.of(
                                        updatedPostProductId, null, null, List.of(), null, 2)));

        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "data.json",
                        APPLICATION_JSON_VALUE,
                        mapper.writeValueAsString(postRequest).getBytes(StandardCharsets.UTF_8));

        List<MockMultipartFile> postImageFiles =
                List.of(
                        new MockMultipartFile(
                                "postImageFiles",
                                "test_01.jpg",
                                MediaType.IMAGE_JPEG_VALUE,
                                "test_01".getBytes()));
        List<MockMultipartFile> productImageFiles = List.of();

        doThrow(new CustomException(ErrorResponseCode.FAIL_TO_UPLOAD_IMAGE))
                .when(s3PostImageUploadPort)
                .uploadImages(any());

        // when
        ResultActions resultActions =
                getResultActions(session, savedPostId, data, postImageFiles, productImageFiles);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.FAIL_TO_UPLOAD_IMAGE.getHttpStatus().value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(7));
    }

    @Test
    @Transactional
    void 게시글_수정_실패7() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(1000L),
                        List.of(),
                        List.of(),
                        List.of());

        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "data.json",
                        APPLICATION_JSON_VALUE,
                        mapper.writeValueAsString(postRequest).getBytes(StandardCharsets.UTF_8));

        // when
        ResultActions resultActions =
                getResultActions(session, savedPostId, data, List.of(), List.of());

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.UNAUTHORIZED_POST_IMAGE_ACCESS
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(8));
    }

    @Test
    @Transactional
    void 게시글_수정_실패8() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(1000L),
                        List.of(),
                        List.of());

        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "data.json",
                        APPLICATION_JSON_VALUE,
                        mapper.writeValueAsString(postRequest).getBytes(StandardCharsets.UTF_8));

        // when
        ResultActions resultActions =
                getResultActions(session, savedPostId, data, List.of(), List.of());

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.UNAUTHORIZED_POST_PRODUCT_ACCESS
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(9));
    }

    @Test
    @Transactional
    void 게시글_수정_실패9() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        Long updatedPostProductId = savedPostProductIds.get(0);
        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(
                                UpdatePostProductRequest.of(
                                        updatedPostProductId,
                                        "상품 이름 수정",
                                        null,
                                        List.of(),
                                        1000L,
                                        -1)));

        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "data.json",
                        APPLICATION_JSON_VALUE,
                        mapper.writeValueAsString(postRequest).getBytes(StandardCharsets.UTF_8));

        // when
        ResultActions resultActions =
                getResultActions(session, savedPostId, data, List.of(), List.of());

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.UNAUTHORIZED_POST_PRODUCT_IMAGE_ACCESS
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(10));
    }

    @Test
    @Transactional
    void 게시글_수정_실패10() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        Long updatedPostProductId = savedPostProductIds.get(0);
        List<PostProductImage> postProductImages =
                loadPostProductImagePort.loadPostProductImagesByPostProductIds(
                        FindByIdsQuery.from(savedPostProductIds));
        Long deletedPostProductImageId = postProductImages.get(0).getId();
        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(
                                UpdatePostProductRequest.of(
                                        updatedPostProductId,
                                        "상품 이름 수정",
                                        null,
                                        List.of(),
                                        deletedPostProductImageId,
                                        -1)));

        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "data.json",
                        APPLICATION_JSON_VALUE,
                        mapper.writeValueAsString(postRequest).getBytes(StandardCharsets.UTF_8));

        // when
        ResultActions resultActions =
                getResultActions(session, savedPostId, data, List.of(), List.of());

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.CANNOT_DELETE_DEFAULT_IMAGE
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(11));
    }

    @Test
    @Transactional
    void 게시글_수정_실패11() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        List<PostProductImage> productImages =
                loadPostProductImagePort.loadPostProductImagesByPostProductIds(
                        FindByIdsQuery.from(savedPostProductIds));
        PostProductImage updateProductImage = productImages.get(0);
        updateProductImage.updateObjectKey("products/test_01.jpg");

        updatePostProductImagePort.updatePostProductImages(List.of(updateProductImage));

        UpdatePostRequest postRequest =
                UpdatePostRequest.of(
                        "게시글 제목 수정",
                        null,
                        List.of(),
                        "게시글 내용 수정",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(
                                UpdatePostProductRequest.of(
                                        updateProductImage.getPostProductId(),
                                        null,
                                        null,
                                        List.of(),
                                        null,
                                        1)));

        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "data.json",
                        APPLICATION_JSON_VALUE,
                        mapper.writeValueAsString(postRequest).getBytes(StandardCharsets.UTF_8));

        List<MockMultipartFile> productImageFiles =
                List.of(
                        new MockMultipartFile(
                                "productImageFiles",
                                "test_01.jpg",
                                IMAGE_JPEG_VALUE,
                                "test_01".getBytes()));

        doReturn(List.of("products/test_01.jpg"))
                .when(s3PostProductImageUploadPort)
                .uploadImages(new ArrayList<>(productImageFiles));

        // when
        ResultActions resultActions =
                getResultActions(session, savedPostId, data, List.of(), productImageFiles);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.POST_PRODUCT_IMAGE_ALREADY_EXISTS
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(12));
    }
}
