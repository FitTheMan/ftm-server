package com.ftm.server.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostRequest;
import com.ftm.server.adapter.in.web.user.dto.request.CreateBookmarkRequest;
import com.ftm.server.adapter.out.persistence.repository.BookmarkRepository;
import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.application.port.in.user.CreateBookmarkUseCase;
import com.ftm.server.application.port.out.persistence.post.SavePostPort;
import com.ftm.server.application.port.out.persistence.user.SaveBookmarkPort;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.Bookmark;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.User;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateBookmarkTest extends BaseTest {

    @Autowired private PlatformTransactionManager transactionManager;

    private TransactionStatus transactionStatus;

    @Autowired private SavePostPort savePostPort;
    @Autowired private SaveBookmarkPort saveBookmarkPort;
    @Autowired private CreateBookmarkUseCase createBookmarkUseCase;
    @Autowired private BookmarkRepository bookmarkRepository;

    private static MockHttpSession session;
    private static User user;
    private static Post post;

    private final List<FieldDescriptor> responseFieldDescriptors =
            List.of(
                    fieldWithPath("status").type(NUMBER).description("응답 상태"),
                    fieldWithPath("code").type(JsonFieldType.STRING).description("상태 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                    fieldWithPath("data")
                            .type(JsonFieldType.OBJECT)
                            .optional()
                            .description("data"));
    private final List<FieldDescriptor> requestFieldDescriptors =
            List.of(fieldWithPath("postId").type(NUMBER).description("북마크 생성 게시글 id"));

    private ResultActions getResultActions(MockHttpSession session, CreateBookmarkRequest request)
            throws Exception {
        return mockMvc.perform( // api 실행
                RestDocumentationRequestBuilders.post("/api/users/bookmarks")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session));
    }

    // 문서화 반환 함수
    private RestDocumentationResultHandler getDocument(Integer identifier) {
        return document(
                "createBookmark/" + identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint(), getModifiedHeader()),
                responseFields(responseFieldDescriptors),
                requestFields(requestFieldDescriptors),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("회원")
                                .summary("북마크 생성 api")
                                .description("북마크를 생성합니다.")
                                .responseFields(responseFieldDescriptors)
                                .build()));
    }

    @BeforeAll
    public void startTransaction() {
        // 수동으로 트랜잭션 시작
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
        // 트랜잭션 시작
        transactionStatus = transactionManager.getTransaction(def);

        SessionAndUser sessionAndUser = createUserAndLoginAndReturnUser(); // 로그인 처리
        session = sessionAndUser.mockHttpSession();
        user = sessionAndUser.user();

        // test 용 post 생성
        post =
                savePostPort.savePost(
                        Post.create(
                                SavePostCommand.from(
                                        user.getId(),
                                        new SavePostRequest(
                                                "test",
                                                new ArrayList<>(),
                                                "content",
                                                new ArrayList<>()),
                                        new ArrayList<>(),
                                        new ArrayList<>())));
    }

    @AfterAll
    public void rollbackTransaction() {
        // 트랜잭션 롤백
        if (transactionStatus != null) {
            transactionManager.rollback(transactionStatus);
        }
    }

    @Test
    @Transactional
    @DisplayName("북마크 생성 성공1")
    void test1() throws Exception {

        // when
        ResultActions resultActions =
                getResultActions(session, new CreateBookmarkRequest(post.getId()));

        // then
        resultActions.andExpect(status().isCreated());

        // documentation
        resultActions.andDo(getDocument(1));
    }

    @Test
    @Transactional
    @DisplayName("북마크 생성 성공2")
    void test2() throws Exception {
        // given
        saveBookmarkPort.saveBookmark(Bookmark.createBookmark(user.getId(), post.getId()));

        // when
        ResultActions resultActions =
                getResultActions(session, new CreateBookmarkRequest(post.getId()));

        // then
        resultActions.andExpect(status().isOk());

        // documentation
        resultActions.andDo(getDocument(2));
    }

    @Test
    @Transactional
    @DisplayName("북마크 생성 실패")
    void test3() throws Exception {
        // when
        ResultActions resultActions = getResultActions(session, new CreateBookmarkRequest(10L));

        // then
        resultActions
                .andExpect(status().is(ErrorResponseCode.POST_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("code").value(ErrorResponseCode.POST_NOT_FOUND.getCode()));

        // documentation
        resultActions.andDo(getDocument(3));
    }

    @Test
    @Transactional
    @DisplayName("레파지토리 테스트")
    void test4() {
        saveBookmarkPort.saveBookmark(Bookmark.createBookmark(user.getId(), post.getId()));
        int result = bookmarkRepository.saveOrUpdate(user.getId(), post.getId());
        assertThat(result).isEqualTo(0);
    }
}
