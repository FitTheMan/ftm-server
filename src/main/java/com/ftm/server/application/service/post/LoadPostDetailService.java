package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.post.LoadPostDetailUseCase;
import com.ftm.server.application.port.out.persistence.post.*;
import com.ftm.server.application.query.*;
import com.ftm.server.application.vo.post.PostDetailVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadPostDetailService implements LoadPostDetailUseCase {

    private final LoadPostPort loadPostPort;
    private final LoadPostImagePort loadPostImagePort;
    private final LoadPostProductPort loadPostProductPort;
    private final LoadPostProductImagePort loadPostProductImagePort;
    private final LoadUserForPostPort loadUserForPostPort;
    private final LoadUserImageForPostPort loadUserImageForPostPort;
    private final UpdatePostPort updatePostPort;
    private final LoadPostLikePort loadPostLikePort;

    @Override
    @Transactional
    public PostDetailVo execute(Long userId, FindByIdQuery query) {
        // 게시글 조회
        Post post =
                loadPostPort
                        .loadPost(query)
                        .orElseThrow(() -> new CustomException(ErrorResponseCode.POST_NOT_FOUND));
        post.validateDeleted();

        // 조회수 업데이트
        post.updateViewCount(post.getViewCount() + 1);
        updatePostPort.updatePost(post);

        // 유저 조회
        User user =
                loadUserForPostPort
                        .loadUserById(FindByIdQuery.of(post.getUserId()))
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);

        // 유저 이미지 조회
        UserImage userImage =
                loadUserImageForPostPort
                        .loadUserImageByUserId(FindByUserIdQuery.of(user.getId()))
                        .orElseThrow(
                                () -> new CustomException(ErrorResponseCode.USER_IMAGE_NOT_FOUND));

        // 게시글 이미지 목록 조회
        List<PostImage> postImages =
                loadPostImagePort.loadPostImagesByPostId(FindByPostIdQuery.of(post.getId()));

        // 게시글 상품 목록 조회
        List<PostProduct> postProducts =
                loadPostProductPort.loadPostProductsByPostId(FindByPostIdQuery.of(post.getId()));

        // 게시글 상품 별 이미지 Map, 여러 상품의 이미지 정보 한번에 조회
        List<PostProductImage> postProductImages =
                loadPostProductImagePort.loadPostProductImagesByPostProductIds(
                        FindByIdsQuery.from(
                                postProducts.stream().map(PostProduct::getId).toList()));

        Map<Long, PostProductImage> postProductImageMap =
                postProductImages.stream()
                        .collect(
                                Collectors.toMap(
                                        PostProductImage::getPostProductId, Function.identity()));

        Boolean userLikeYn =
                userId != null
                        && loadPostLikePort.findPostLikeByUser(userId, query.getId()).getLikeYn();

        return PostDetailVo.from(
                post, user, userImage, postImages, postProducts, postProductImageMap, userLikeYn);
    }
}
