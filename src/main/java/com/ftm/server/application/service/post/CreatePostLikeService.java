package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.post.CreatePostLikeUseCase;
import com.ftm.server.application.port.out.persistence.post.*;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostLike;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatePostLikeService implements CreatePostLikeUseCase {

    private final UpdatePostPort updatePostPort;
    private final LoadPostPort loadPostPort;
    private final LoadPostLikePort loadPostLikePort;
    private final SavePostLikePort savePostLikePort;
    private final DeletePostLikePort deletePostLikePort;

    @Transactional
    public Boolean execute(Long userId, Long postId) {

        Post post =
                loadPostPort
                        .loadPost(FindByIdQuery.of(postId))
                        .orElseThrow(() -> new CustomException(ErrorResponseCode.POST_NOT_FOUND));

        // 이미 등록된 좋아요 있는지 확인
        Optional<Long> optionalPostLikeId = loadPostLikePort.findOneByUserAndPost(userId, postId);

        // 없으면 좋아요 생성
        if (optionalPostLikeId.isEmpty()) {
            PostLike postLike = PostLike.create(postId, userId);
            savePostLikePort.savePostLike(postLike);
            post.plusLikeCount(); // 게시글 좋아요 숫자 증가
            updatePostPort.updatePost(post);
            return true;
        }

        // 있으면 삭제
        else {
            post.minusLikeCount();
            updatePostPort.updatePost(post); // 게시글 좋아요 숫자 감소
            deletePostLikePort.deletePostLike(optionalPostLikeId.get());
            return false;
        }
    }
}
