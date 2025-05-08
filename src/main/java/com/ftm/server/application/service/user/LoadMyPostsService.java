package com.ftm.server.application.service.user;

import com.ftm.server.application.port.in.user.LoadMyPostsUseCase;
import com.ftm.server.application.port.out.persistence.user.LoadPostImageUserDomainPort;
import com.ftm.server.application.port.out.persistence.user.LoadPostUserDomainPort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindPostsByPagingQuery;
import com.ftm.server.application.vo.post.PostPagingVo;
import com.ftm.server.application.vo.post.PostSummaryVo;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadMyPostsService implements LoadMyPostsUseCase {

    private final LoadPostUserDomainPort loadPostUserDomainPort;
    private final LoadPostImageUserDomainPort loadPostImageUserDomainPort;

    @Override
    public PostPagingVo execute(FindPostsByPagingQuery query) {
        // 페이징된 게시글 목록 조회 (최신순 정렬)
        Slice<Post> posts = loadPostUserDomainPort.loadPostsByUserIdWithPaging(query);
        List<Post> sortedPosts = posts.getContent();

        List<Long> postIds = sortedPosts.stream().map(Post::getId).toList();

        // 각 게시글의 이미지 중 대표(썸네일) 이미지 한개씩만 조회
        List<PostImage> postImages =
                loadPostImageUserDomainPort.loadRepresentativeImagesByPostIds(
                        FindByIdsQuery.from(postIds));

        // postId -> 대표 이미지(PostImage) 매핑 (createdAt 기준)
        Map<Long, PostImage> postIdToImageMap =
                postImages.stream()
                        .collect(
                                Collectors.toMap(
                                        PostImage::getPostId,
                                        Function.identity(),
                                        BinaryOperator.minBy(
                                                Comparator.comparing(PostImage::getCreatedAt))));

        // 정렬된 순서를 보장하면서 게시글, 게시글 이미지 매핑
        List<PostSummaryVo> items =
                sortedPosts.stream()
                        .map(post -> PostSummaryVo.from(post, postIdToImageMap.get(post.getId())))
                        .toList();

        return PostPagingVo.from(items, posts.hasNext());
    }
}
