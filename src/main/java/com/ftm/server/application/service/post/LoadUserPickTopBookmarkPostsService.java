package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.post.LoadUserPickTopBookmarkPostsUseCase;
import com.ftm.server.application.port.out.cache.LoadUserPickTopBookmarkPostsWithCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostImagePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostWithBookmarkCountPort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.vo.post.LoadUserPickTopBookmarkPostsVo;
import com.ftm.server.application.vo.post.PostWithIdAndAuthorVo;
import com.ftm.server.application.vo.post.PostWithUserAndBookmarkCountVo;
import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.PostImage;
import com.ftm.server.domain.enums.PostHashtag;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadUserPickTopBookmarkPostsService implements LoadUserPickTopBookmarkPostsUseCase {

    private final LoadUserPickTopBookmarkPostsWithCachePort cachePort;
    private final LoadPostWithBookmarkCountPort loadPostWithBookmarkCountPort;
    private final LoadPostImagePort loadPostImagePort;

    @Override
    public List<LoadUserPickTopBookmarkPostsVo> execute() {
        // 1) 캐시에서 상위 id 목록 조회 (4개)
        List<PostWithIdAndAuthorVo> postList = cachePort.getTopBookmarkPosts();
        if (postList.isEmpty()) return List.of();

        // 2) id 목록
        List<Long> postIds = postList.stream().map(PostWithIdAndAuthorVo::getPostId).toList();

        // 3) 상세 조회 (유저/북마크 수 포함)
        Map<Long, PostWithUserAndBookmarkCountVo> detailPostMap =
                loadPostWithBookmarkCountPort
                        .loadPostWithUserAndBookmarkCount(FindByIdsQuery.from(postIds))
                        .stream()
                        .collect(Collectors.toMap(PostWithUserAndBookmarkCountVo::getId, vo -> vo));

        // 4) 대표 이미지
        List<PostImage> postImages =
                loadPostImagePort.loadRepresentativeImagesByPostIds(FindByIdsQuery.from(postIds));
        Map<Long, String> imageUrlMap =
                postImages.stream()
                        .collect(
                                java.util.stream.Collectors.toMap(
                                        PostImage::getPostId,
                                        PostImage::getObjectKey,
                                        (a, b) -> a));

        // 5) 합치기 (postList 순서 = 랭킹)
        return IntStream.range(0, postIds.size())
                .mapToObj(
                        i -> {
                            Long postId = postIds.get(i);
                            PostWithUserAndBookmarkCountVo p = detailPostMap.get(postId);
                            int ranking = i + 1;
                            String imageUrl =
                                    imageUrlMap.getOrDefault(
                                            p.getId(), PropertiesHolder.POST_DEFAULT_IMAGE);
                            List<String> hashtags =
                                    p.getHashtags() == null || p.getHashtags().length == 0
                                            ? List.of()
                                            : Arrays.stream(p.getHashtags())
                                                    .map(PostHashtag::getTag)
                                                    .toList();
                            return LoadUserPickTopBookmarkPostsVo.of(
                                    ranking,
                                    p,
                                    PropertiesHolder.CDN_PATH + "/" + imageUrl,
                                    hashtags);
                        })
                .toList();
    }
}
