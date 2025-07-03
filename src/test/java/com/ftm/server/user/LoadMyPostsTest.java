package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
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
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

public class LoadMyPostsTest extends BaseTest {

    @MockitoSpyBean private S3PostImageUploadPort s3PostImageUploadPort;
    @MockitoSpyBean private S3PostProductImageUploadPort s3PostProductImageUploadPort;
    @MockitoSpyBean private S3ImageDeletePort s3ImageDeletePort;
    @MockitoSpyBean private AfterRollbackExecutorPort afterRollbackExecutorPort;
    @Autowired private SavePostUseCase savePostUseCase;

    private final ParameterDescriptor queryParametersForPage =
            parameterWithName("page")
                    .optional()
                    .description("페이지 번호")
                    .attributes(
                            new Attributes.Attribute("constraint", "숫자, 최소 0 이상"),
                            new Attributes.Attribute("default", "0"));

    private final ParameterDescriptor queryParametersForSize =
            parameterWithName("size")
                    .optional()
                    .description("페이지당 개수")
                    .attributes(
                            new Attributes.Attribute("constraint", "숫자, 최소 1 이상 최대 10 이하"),
                            new Attributes.Attribute("default", "5"));

    private final List<FieldDescriptor> responseFieldLoadMyPosts =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"),
                    fieldWithPath("data.items[]").type(ARRAY).optional().description("작성한 게시글 목록"),
                    fieldWithPath("data.items[].id").type(NUMBER).description("게시글 ID"),
                    fieldWithPath("data.items[].title").type(STRING).description("게시글 제목"),
                    fieldWithPath("data.items[].createdAt").type(STRING).description("게시글 생성 날짜"),
                    fieldWithPath("data.items[].updatedAt").type(STRING).description("게시글 수정 날짜"),
                    fieldWithPath("data.items[].imageUrl")
                            .type(STRING)
                            .description("게시글 대표 이미지 URL"),
                    fieldWithPath("data.hasNext").type(BOOLEAN).description("다음 페이지 데이터 존재여부"));

    private ResultActions getResultActions(MockHttpSession session, int page, int size)
            throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/users/me/posts")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .session(session));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "loadMyPosts/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldLoadMyPosts),
                queryParameters(queryParametersForPage, queryParametersForSize),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("작성한 유저픽 게시글 목록 조회 api")
                                .description("작성한 유저픽 게시글 목록을 조회하는 api 입니다.")
                                .responseFields(responseFieldLoadMyPosts)
                                .queryParameters(queryParametersForPage, queryParametersForSize)
                                .build()));
    }

    @BeforeEach
    void set_up() throws Exception {
        User user = createTestUser("test@gmail.com", "test1234!");

        // s3 실제 호출 대신 mock 대입
        doReturn(List.of()).when(s3PostImageUploadPort).uploadImages(new ArrayList<>(List.of()));
        doReturn(List.of())
                .when(s3PostProductImageUploadPort)
                .uploadImages(new ArrayList<>(List.of()));
        doNothing().when(s3ImageDeletePort).deleteImages(any());
        doNothing().when(afterRollbackExecutorPort).doAfterRollback(any());

        for (int i = 0; i < 5; i++) {
            SavePostRequest postRequest =
                    new SavePostRequest(
                            "독도 토너 추천 " + i,
                            List.of(PostHashtag.CLEANSING),
                            "<div>test</div>",
                            List.of(new SavePostProductRequest(-1, "독도 토너", "라운드랩", List.of())));
            savePostUseCase.execute(
                    SavePostCommand.from(user.getId(), postRequest, List.of(), List.of()));
        }
    }

    @Test
    @Transactional
    void 작성한_유저픽_게시글_목록_조회_성공() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        // when
        ResultActions resultActions = getResultActions(session, 0, 4);

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 작성한_유저픽_게시글_목록_조회_실패1() throws Exception {
        // when
        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/users/me/posts")
                                .param("page", String.valueOf(0))
                                .param("size", String.valueOf(5)));

        // then
        resultActions.andExpect(
                status().is(ErrorResponseCode.NOT_AUTHENTICATED.getHttpStatus().value()));

        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 작성한_유저픽_게시글_목록_조회_실패2() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        // when
        ResultActions resultActions = getResultActions(session, -1, 5);

        // then
        resultActions.andExpect(
                status().is(
                                ErrorResponseCode.BAD_REQUEST_PAGING_INDEX_RANGE
                                        .getHttpStatus()
                                        .value()));

        resultActions.andDo(getDocument(3));
    }

    @Test
    @Transactional
    void 작성한_유저픽_게시글_목록_조회_실패3() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        // when
        ResultActions resultActions = getResultActions(session, 0, 100);

        // then
        resultActions.andExpect(
                status().is(
                                ErrorResponseCode.BAD_REQUEST_PAGING_SIZE_RANGE
                                        .getHttpStatus()
                                        .value()));

        resultActions.andDo(getDocument(4));
    }
}
