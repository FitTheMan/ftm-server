package com.ftm.server.application.service.post;

import com.ftm.server.application.command.post.UpdatePostCommand;
import com.ftm.server.application.port.in.post.UpdatePostUseCase;
import com.ftm.server.application.port.out.persistence.post.*;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.service.post.image.UpdatePostImageService;
import com.ftm.server.application.service.post.product.UpdatePostProductService;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdatePostService implements UpdatePostUseCase {

    private final LoadPostPort loadPostPort;
    private final UpdatePostPort updatePostPort;

    private final UpdatePostImageService updatePostImageService;
    private final UpdatePostProductService updatePostProductService;

    @Override
    @Transactional
    public void execute(UpdatePostCommand command) {
        // 게시글 조회
        Post post =
                loadPostPort
                        .loadPost(FindByIdQuery.of(command.getId()))
                        .orElseThrow(() -> new CustomException(ErrorResponseCode.POST_NOT_FOUND));
        post.validateDeleted();
        post.validateWriter(command.getUserId());

        // 게시글 업데이트
        post.update(command);
        updatePostPort.updatePost(post);

        // 게시글 이미지 업데이트
        updatePostImageService.execute(
                post, command.getDeletePostImageIds(), command.getPostImageFiles());

        // 상품, 상품 이미지 업데이트
        updatePostProductService.execute(post, command);
    }
}
