package com.ftm.server.post;

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
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

public class DeletePostTest extends BaseTest {

    @Autowired private SavePostUseCase savePostUseCase;
    @MockitoSpyBean private S3PostImageUploadPort s3PostImageUploadPort;
    @MockitoSpyBean private S3PostProductImageUploadPort s3PostProductImageUploadPort;
    @MockitoSpyBean private S3ImageDeletePort s3ImageDeletePort;
    @MockitoSpyBean private AfterRollbackExecutorPort afterRollbackExecutorPort;

    private final ParameterDescriptor pathParametersForPostId =
            parameterWithName("postId").description("게시글 ID");

    private final List<FieldDescriptor> responseFieldDeletePost =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(STRING).description("상태 코드"),
                    fieldWithPath("message").type(STRING).description("메시지"),
                    fieldWithPath("data").type(OBJECT).optional().description("응답 데이터"));

    private Long savedPostId;

    private ResultActions getResultActions(MockHttpSession session, Long postId) throws Exception {
        return mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/posts/{postId}", postId)
                        .session(session));
    }

    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "deletePost/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                pathParameters(pathParametersForPostId),
                responseFields(responseFieldDeletePost),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저픽 게시글")
                                .summary("유저픽 게시글 삭제 api")
                                .description("유저픽 게시글 삭제 api 입니다.")
                                .responseFields(responseFieldDeletePost)
                                .build()));
    }

    @BeforeEach
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
    void 게시글_삭제_성공() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        // when
        ResultActions resultActions = getResultActions(session, savedPostId);

        // then
        resultActions.andExpect(status().isOk()).andDo(print());

        // Document
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    void 게시글_삭제_실패1() throws Exception {
        // when
        ResultActions resultActions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.delete(
                                "/api/posts/{postId}", savedPostId));

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.NOT_AUTHENTICATED.getHttpStatus().value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    void 게시글_삭제_실패2() throws Exception {
        // given
        MockHttpSession session = login("test@gmail.com");

        // when
        ResultActions resultActions = getResultActions(session, 100000L);

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.POST_NOT_FOUND.getHttpStatus().value()))
                .andDo(print());

        // document
        resultActions.andDo(getDocument(3));
    }

    @Test
    @Transactional
    void 게시글_삭제_실패3() throws Exception {
        // given
        MockHttpSession session = createUserAndLogin("test12@gmail.com", "ddddd!");

        // when
        ResultActions resultActions = getResultActions(session, savedPostId);

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
}
