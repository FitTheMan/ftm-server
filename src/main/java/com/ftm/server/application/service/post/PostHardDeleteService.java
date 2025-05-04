package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.post.PostHardDeleteUseCase;
import com.ftm.server.application.port.out.persistence.post.*;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.transcation.AfterCommitExecutorPort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindPostByDeleteOptionQuery;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.PostProductImage;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostHardDeleteService implements PostHardDeleteUseCase {

    private final LoadPostPort loadPostPort;
    private final LoadPostImagePort loadPostImagePort;
    private final LoadPostProductPort loadPostProductPort;
    private final LoadPostProductImagePort loadPostProductImagePort;
    private final DeletePostPort deletePostPort;
    private final DeletePostImagePort deletePostImagePort;
    private final DeletePostProductPort deletePostProductPort;
    private final DeletePostProductImagePort deletePostProductImagePort;

    private final S3ImageDeletePort s3ImageDeletePort;
    private final AfterCommitExecutorPort afterCommitExecutorPort;

    @Override
    @Transactional
    public void execute() {
        // 삭제 대상 post 조회 : 이미 삭제가 된 게시글 중 30일이 지난 경우 hard delete 대상
        List<Long> deletedPostIds =
                loadPostPort
                        .loadPostsByDeleteOption(
                                FindPostByDeleteOptionQuery.of(true, LocalDate.now().minusDays(30)))
                        .stream()
                        .map(Post::getId)
                        .toList();

        // 삭제할 post product 조회
        List<Long> deletedPostProductIds =
                loadPostProductPort
                        .loadPostProductsByPostIds(FindByIdsQuery.from(deletedPostIds))
                        .stream()
                        .map(PostProduct::getId)
                        .toList();

        // post 관련 엔티티 모두 삭제
        // 1. 상품 이미지 삭제
        List<PostProductImage> postProductImages =
                loadPostProductImagePort.loadPostProductImagesByPostProductIds(
                        FindByIdsQuery.from(deletedPostProductIds));
        List<String> deleteProductImageObjectKeys =
                postProductImages.stream().map(PostProductImage::getObjectKey).toList();
        registerCommitHook(deleteProductImageObjectKeys);
        deletePostProductImagePort.deletePostProductImages(postProductImages);

        // 2. 상품 삭제
        deletePostProductPort.deletePostProductsByIds(deletedPostProductIds);

        // 3. 게시글 이미지 삭제
        List<PostImage> postImages =
                loadPostImagePort.loadPostImagesByPostIds(FindByIdsQuery.from(deletedPostIds));
        List<String> deletePostImageObjectKeys =
                postImages.stream().map(PostImage::getObjectKey).toList();
        registerCommitHook(deletePostImageObjectKeys);
        deletePostImagePort.deletePostImages(postImages);

        // 4. 게시글 삭제
        deletePostPort.deletePostsByIds(deletedPostIds);
    }

    private void registerCommitHook(List<String> deleteImageObjectKeys) {
        afterCommitExecutorPort.doAfterCommit(
                () -> s3ImageDeletePort.deleteImages(deleteImageObjectKeys));
    }
}
