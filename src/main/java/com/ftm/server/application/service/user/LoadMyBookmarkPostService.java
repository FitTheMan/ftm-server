package com.ftm.server.application.service.user;

import com.ftm.server.application.port.in.user.LoadMyBookmarkPostUseCase;
import com.ftm.server.application.port.out.persistence.user.LoadBookmarkPort;
import com.ftm.server.application.port.out.persistence.user.LoadPostImageUserDomainPort;
import com.ftm.server.application.port.out.persistence.user.LoadPostUserDomainPort;
import com.ftm.server.application.query.FindBookmarksByPagingQuery;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.vo.post.PostPagingVo;
import com.ftm.server.application.vo.post.PostSummaryVo;
import com.ftm.server.domain.entity.Bookmark;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadMyBookmarkPostService implements LoadMyBookmarkPostUseCase {

    private final LoadBookmarkPort loadBookmarkPort;
    private final LoadPostUserDomainPort loadPostUserDomainPort;
    private final LoadPostImageUserDomainPort loadPostImageUserDomainPort;

    @Override
    public PostPagingVo execute(FindBookmarksByPagingQuery query) {
        Slice<Bookmark> bookmarks = loadBookmarkPort.loadBookmarksByUserIdWithPaging(query);
        List<Long> postIds = bookmarks.getContent().stream().map(Bookmark::getPostId).toList();
        FindByIdsQuery postIdsQuery = FindByIdsQuery.from(postIds);

        // 북마크한 유저픽 게시글 목록 조회
        // 최신순으로 정렬된 북마크 목록의 순서를 보장 -> postId : Post Map 매핑
        List<Post> posts = loadPostUserDomainPort.loadPostsByIds(postIdsQuery);
        Map<Long, Post> postMap =
                posts.stream().collect(Collectors.toMap(Post::getId, Function.identity()));

        // 게시글 이미지 목록 조회 (대표 이미지)
        List<PostImage> postImages =
                loadPostImageUserDomainPort.loadRepresentativeImagesByPostIds(postIdsQuery);
        Map<Long, PostImage> postIdToImageMap =
                postImages.stream()
                        .collect(
                                Collectors.toMap(
                                        PostImage::getPostId,
                                        Function.identity(),
                                        BinaryOperator.minBy(
                                                Comparator.comparing(PostImage::getCreatedAt))));

        // 최신순으로 정렬된 postIds 를 기준으로 items 생성
        List<PostSummaryVo> items =
                postIds.stream()
                        .map(
                                postId -> {
                                    Post post = postMap.get(postId);
                                    PostImage postImage = postIdToImageMap.get(postId);
                                    return PostSummaryVo.from(post, postImage);
                                })
                        .toList();

        return PostPagingVo.from(items, bookmarks.hasNext());
    }
}
