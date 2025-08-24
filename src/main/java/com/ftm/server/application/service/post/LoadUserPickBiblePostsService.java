package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.post.LoadUserPickBiblePostsUseCase;
import com.ftm.server.application.port.out.cache.LoadUserPickBiblePostsWithCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostImagePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostWithBookmarkCountPort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.vo.post.*;
import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.PostImage;
import com.ftm.server.domain.enums.PostHashtag;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadUserPickBiblePostsService implements LoadUserPickBiblePostsUseCase {

    private final LoadUserPickBiblePostsWithCachePort loadUserPickBibleWithCachePort;

    private final LoadPostWithBookmarkCountPort loadPostWithBookmarkCountPort;
    private final LoadPostImagePort loadPostImagePort;

    @Override
    public List<UserPickBiblePostsVo> execute() {
        // 1. 좋아요 누적 순으로 상위 4개의 게시물을 cache 에서 조회
        List<PostWithIdAndAuthorVo> postList =
                loadUserPickBibleWithCachePort.getUserPickBiblePost();

        // 2) id 목록
        List<Long> postIds = postList.stream().map(PostWithIdAndAuthorVo::getPostId).toList();

        // 3) post 상세 조회
        Map<Long, PostWithUserAndBookmarkCountVo> detailPostMap =
                loadPostWithBookmarkCountPort
                        .loadPostWithUserAndBookmarkCount(FindByIdsQuery.from(postIds))
                        .stream()
                        .collect(Collectors.toMap(PostWithUserAndBookmarkCountVo::getId, vo -> vo));

        // 4) 대표 이미지
        List<PostImage> postImages =
                loadPostImagePort.loadRepresentativeImagesByPostIds(FindByIdsQuery.from(postIds));
        var imageUrlMap =
                postImages.stream()
                        .collect(
                                java.util.stream.Collectors.toMap(
                                        PostImage::getPostId,
                                        PostImage::getObjectKey,
                                        (a, b) -> a // 중복 시 첫 이미지
                                        ));

        // 5) 합치기 (postList 순서 = 랭킹)
        return IntStream.range(0, postIds.size())
                .mapToObj(
                        i -> {
                            Long postId = postIds.get(i);
                            PostWithUserAndBookmarkCountVo p = detailPostMap.get(postId);
                            int ranking = i + 1;
                            String imageUrl =
                                    imageUrlMap.getOrDefault(
                                            p.getId(),
                                            PropertiesHolder.POST_DEFAULT_IMAGE); // 없으면 null
                            List<String> hashtags =
                                    p.getHashtags() == null || p.getHashtags().length == 0
                                            ? List.of()
                                            : Arrays.stream(p.getHashtags())
                                                    .map(PostHashtag::getTag)
                                                    .toList();
                            return UserPickBiblePostsVo.of(
                                    ranking,
                                    p,
                                    PropertiesHolder.CDN_PATH + "/" + imageUrl,
                                    hashtags);
                        })
                .toList();
    }
}
