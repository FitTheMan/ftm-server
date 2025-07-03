package com.ftm.server.post;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostProductRequest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostRequest;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.s3.S3PostImageUploadPort;
import com.ftm.server.application.port.out.s3.S3PostProductImageUploadPort;
import com.ftm.server.application.port.out.transcation.AfterRollbackExecutorPort;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

public class SavePostTest extends BaseTest {

    @MockitoSpyBean private S3PostImageUploadPort s3PostImageUploadPort;
    @MockitoSpyBean private S3PostProductImageUploadPort s3PostProductImageUploadPort;
    @MockitoSpyBean private S3ImageDeletePort s3ImageDeletePort;
    @MockitoSpyBean private AfterRollbackExecutorPort afterRollbackExecutorPort;

    private final List<RequestPartDescriptor> requestPartSavePost =
            List.of(
                    partWithName("data")
                            .description("게시글 저장 정보")
                            .attributes(
                                    new Attributes.Attribute("content-type", "application/json")),
                    partWithName("postImageFiles")
                            .optional()
                            .description("게시글 이미지 파일 리스트 (List<MultipartFile> 형태, 순서를 보장)")
                            .attributes(new Attributes.Attribute("content-type", "image/*")),
                    partWithName("productImageFiles")
                            .optional()
                            .description("상품 이미지 파일 리스트 (List<MultipartFile> 형태, 순서를 보장)")
                            .attributes(new Attributes.Attribute("content-type", "image/*")));

    private final List<FieldDescriptor> requestPartFieldSavePost =
            List.of(
                    fieldWithPath("title").type(STRING).description("게시글 제목"),
                    fieldWithPath("hashtags[]").type(ARRAY).optional().description("게시글 해시태그 목록"),
                    fieldWithPath("content").type(STRING).description("게시글 내용"),
                    fieldWithPath("products[]")
                            .type(ARRAY)
                            .optional()
                            .description("게시글에 포함된 상품 목록"),
                    fieldWithPath("products[].imageIndex")
                            .type(NUMBER)
                            .description(
                                    "상품 이미지와 매핑될 인덱스 (**1**부터 시작, 이미지를 등록하지 않은 상품이라면 **-1** 저장) ** 순서 중요 **"),
                    fieldWithPath("products[].name").type(STRING).description("상품 이름"),
                    fieldWithPath("products[].brand").type(STRING).optional().description("상품 브랜드"),
                    fieldWithPath("products[].hashtags[]")
                            .type(ARRAY)
                            .optional()
                            .description("상품 해시태그 목록"));

    private final List<FieldDescriptor> responseFieldSavePost =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"),
                    fieldWithPath("data.postId").type(NUMBER).description("생성된 유저픽 게시글 ID"),
                    fieldWithPath("data.createdAt").type(STRING).description("게시글 생성 날짜"),
                    fieldWithPath("data.updatedAt").type(STRING).description("게시글 수정 날짜"));

    private ResultActions getResultActions(
            MockHttpSession session,
            MockMultipartFile data,
            List<MockMultipartFile> postImageFiles,
            List<MockMultipartFile> productImageFiles)
            throws Exception {
        MockMultipartHttpServletRequestBuilder builder =
                (MockMultipartHttpServletRequestBuilder)
                        RestDocumentationRequestBuilders.multipart("/api/posts")
                                .file(data) // JSON part ("data")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .session(session)
                                .with(
                                        request -> {
                                            request.setMethod("POST");
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

    private ResultActions getResultActions(MockMultipartFile data) throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.multipart("/api/posts")
                        .file(data) // JSON part ("data")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(
                                request -> {
                                    request.setMethod("POST");
                                    return request;
                                }));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "savePost/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                requestParts(requestPartSavePost),
                requestPartFields("data", requestPartFieldSavePost),
                responseFields(responseFieldSavePost),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저픽 게시글")
                                .summary("유저픽 게시글 저장 api")
                                .description("유저픽 게시글 저장 api 입니다.")
                                .responseFields(responseFieldSavePost)
                                .build()));
    }

    @Test
    @Transactional
    void 게시글_저장_성공() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();

        SavePostRequest postRequest =
                new SavePostRequest(
                        "독도 토너 추천",
                        List.of(),
                        "<dev></dev>",
                        List.of(
                                new SavePostProductRequest(1, "독도 토너", "라운드랩", List.of()),
                                new SavePostProductRequest(2, "시카 크림", "더하르나이", List.of()),
                                new SavePostProductRequest(-1, "수분진정 앰플", "바이오던스", List.of())));
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
                                IMAGE_JPEG_VALUE,
                                "test_01".getBytes()),
                        new MockMultipartFile(
                                "postImageFiles",
                                "test_02.jpg",
                                IMAGE_JPEG_VALUE,
                                "test_02".getBytes()));

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
        doReturn(List.of("posts/test_01.jpg", "posts/test_02.jpg"))
                .when(s3PostImageUploadPort)
                .uploadImages(new ArrayList<>(postImageFiles));
        doReturn(List.of("products/test_01.jpg", "products/test_02.jpg"))
                .when(s3PostProductImageUploadPort)
                .uploadImages(new ArrayList<>(productImageFiles));

        doNothing().when(s3ImageDeletePort).deleteImages(any());
        doNothing().when(afterRollbackExecutorPort).doAfterRollback(any());

        // when
        ResultActions resultActions =
                getResultActions(session, data, postImageFiles, productImageFiles);

        // then
        resultActions.andExpect(status().isCreated()).andDo(print());

        // document
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 게시글_저장_실패1() throws Exception {
        // given
        SavePostRequest postRequest =
                new SavePostRequest(
                        "",
                        List.of(),
                        "",
                        List.of(
                                new SavePostProductRequest(-1, "", "라운드랩", List.of()),
                                new SavePostProductRequest(-1, "시카 크림", "더하르나이", List.of()),
                                new SavePostProductRequest(-1, "수분진정 앰플", "바이오던스", List.of())));
        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "data.json",
                        APPLICATION_JSON_VALUE,
                        mapper.writeValueAsString(postRequest).getBytes(StandardCharsets.UTF_8));

        // when
        ResultActions resultActions = getResultActions(data);

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHENTICATED.getHttpStatus().value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 게시글_저장_실패2() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();

        SavePostRequest postRequest =
                new SavePostRequest(
                        "독도 토너 추천",
                        List.of(),
                        "<dev></dev>",
                        List.of(
                                new SavePostProductRequest(1, "독도 토너", "라운드랩", List.of()),
                                new SavePostProductRequest(1, "시카 크림", "더하르나이", List.of())));
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
                getResultActions(session, data, postImageFiles, productImageFiles);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.INVALID_POST_PRODUCT_IMAGE_MAPPING
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(3));
    }

    @Test
    @Transactional
    void 게시글_저장_실패3() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();

        SavePostRequest postRequest =
                new SavePostRequest(
                        "",
                        List.of(),
                        "",
                        List.of(
                                new SavePostProductRequest(-1, "", "라운드랩", List.of()),
                                new SavePostProductRequest(-1, "시카 크림", "더하르나이", List.of()),
                                new SavePostProductRequest(-1, "수분진정 앰플", "바이오던스", List.of())));
        MockMultipartFile data =
                new MockMultipartFile(
                        "data",
                        "data.json",
                        APPLICATION_JSON_VALUE,
                        mapper.writeValueAsString(postRequest).getBytes(StandardCharsets.UTF_8));

        List<MockMultipartFile> postImageFiles = List.of();
        List<MockMultipartFile> productImageFiles = List.of();

        // when
        ResultActions resultActions =
                getResultActions(session, data, postImageFiles, productImageFiles);

        // then
        resultActions
                .andExpect(
                        status().is(
                                        ErrorResponseCode.INVALID_REQUEST_ARGUMENT
                                                .getHttpStatus()
                                                .value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(4));
    }

    @Test
    @Transactional
    void 게시글_저장_실패4() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();

        SavePostRequest postRequest =
                new SavePostRequest("독도 토너 추천", List.of(), "<dev></dev>", List.of());
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
                getResultActions(session, data, postImageFiles, productImageFiles);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.INVALID_IMAGE_FORMAT.getHttpStatus().value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(5));
    }

    @Test
    @Transactional
    void 게시글_저장_실패5() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin();

        SavePostRequest postRequest =
                new SavePostRequest("독도 토너 추천", List.of(), "<dev></dev>", List.of());
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
                getResultActions(session, data, postImageFiles, productImageFiles);

        // then
        resultActions
                .andExpect(
                        status().is(ErrorResponseCode.FAIL_TO_UPLOAD_IMAGE.getHttpStatus().value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(6));
    }
}
