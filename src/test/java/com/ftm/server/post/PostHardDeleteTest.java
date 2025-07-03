package com.ftm.server.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import com.ftm.server.BaseTest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostProductRequest;
import com.ftm.server.adapter.in.web.post.dto.request.SavePostRequest;
import com.ftm.server.adapter.out.persistence.repository.PostRepository;
import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.application.port.in.post.PostHardDeleteUseCase;
import com.ftm.server.application.port.in.post.SavePostUseCase;
import com.ftm.server.application.port.out.persistence.post.LoadPostPort;
import com.ftm.server.application.port.out.persistence.post.UpdatePostPort;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.s3.S3PostImageUploadPort;
import com.ftm.server.application.port.out.s3.S3PostProductImageUploadPort;
import com.ftm.server.application.port.out.transcation.AfterRollbackExecutorPort;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.vo.post.PostInfoVo;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.enums.PostHashtag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

public class PostHardDeleteTest extends BaseTest {

    @Autowired private SavePostUseCase savePostUseCase;
    @Autowired private PostHardDeleteUseCase postHardDeleteUseCase;
    @Autowired private LoadPostPort loadPostPort;
    @Autowired private UpdatePostPort updatePostPort;
    @Autowired private PostRepository postRepository;

    @MockitoSpyBean private S3PostImageUploadPort s3PostImageUploadPort;
    @MockitoSpyBean private S3PostProductImageUploadPort s3PostProductImageUploadPort;
    @MockitoSpyBean private S3ImageDeletePort s3ImageDeletePort;
    @MockitoSpyBean private AfterRollbackExecutorPort afterRollbackExecutorPort;

    @PersistenceContext private EntityManager em;

    private Long savedPostId;

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
    void 게시글_hard_delete_성공() throws Exception {
        // given
        Post post = loadPostPort.loadPost(FindByIdQuery.of(savedPostId)).get();

        post.updateIsDeleted(true);
        post.updateDeletedAt(LocalDateTime.now().minusDays(30));
        updatePostPort.updatePost(post);

        // when
        postHardDeleteUseCase.execute();

        em.flush();
        em.clear();

        // then
        assertThat(postRepository.findById(savedPostId)).isEmpty();
    }
}
