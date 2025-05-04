package com.ftm.server.application.service.post;

import com.ftm.server.application.command.post.DeletePostCommand;
import com.ftm.server.application.port.in.post.DeletePostUseCase;
import com.ftm.server.application.port.out.persistence.post.LoadPostPort;
import com.ftm.server.application.port.out.persistence.post.UpdatePostPort;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.Post;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeletePostService implements DeletePostUseCase {

    private final LoadPostPort loadPostPort;
    private final UpdatePostPort updatePostPort;

    @Override
    @Transactional
    public void execute(DeletePostCommand command) {
        Post post =
                loadPostPort
                        .loadPost(FindByIdQuery.of(command.getPostId()))
                        .orElseThrow(() -> new CustomException(ErrorResponseCode.POST_NOT_FOUND));
        post.validateWriter(command.getUserId());

        // 게시글 삭제 필드 업데이트
        post.updateIsDeleted(true);
        post.updateDeletedAt(LocalDateTime.now());

        updatePostPort.updatePost(post);
    }
}
