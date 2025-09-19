package com.ftm.server.application.service.post;

import static java.util.stream.Collectors.toMap;

import com.ftm.server.application.port.in.post.LoadUserPickTopBookmarkPostsUseCase;
import com.ftm.server.application.port.out.cache.LoadUserPickTopBookmarkPostsWithCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostImagePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostPort;
import com.ftm.server.application.port.out.persistence.post.LoadPostWithBookmarkCountPort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindByPostIdsAndUserQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.post.*;
import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.PostImage;
import com.ftm.server.domain.enums.PostHashtag;
import java.util.*;
import java.util.function.Function;
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
    private final LoadPostPort loadPostPort;

    @Override
    public List<LoadUserPickTopBookmarkPostsVo> execute(FindByUserIdQuery query) {
        // 1) 캐시에서 상위 id 목록 조회 (4개)
        List<Long> postIds = cachePort.getTopBookmarkPosts();

        if (postIds.isEmpty()) return List.of();

        return convertToVo(postIds, query.getUserId());
    }

    private List<LoadUserPickTopBookmarkPostsVo> convertToVo(List<Long> postIds, Long userId) {

        // post 상세 조회
        Map<Long, PostWithUserAndBookmarkCountVo> detailPostMap =
                loadPostWithBookmarkCountPort
                        .loadPostWithUserAndBookmarkCount(FindByIdsQuery.from(postIds))
                        .stream()
                        .collect(toMap(PostWithUserAndBookmarkCountVo::getId, vo -> vo));

        Map<Long, Boolean> userBookmarkMap;
        if (userId != null) {
            userBookmarkMap =
                    loadPostPort
                            .loadPostIdAndBookmarkYn(FindByPostIdsAndUserQuery.of(postIds, userId))
                            .stream()
                            .collect(
                                    toMap(
                                            PostIdAndBookmarkYnVo::getPostId,
                                            PostIdAndBookmarkYnVo::getBookmarkYn));
            ;
        } else {
            userBookmarkMap =
                    postIds.stream().collect(Collectors.toMap(Function.identity(), id -> false));
        }

        // post 대표 이미지 조회
        Map<Long, String> imageUrlMap =
                loadPostImagePort
                        .loadRepresentativeImagesByPostIds(FindByIdsQuery.from(postIds))
                        .stream()
                        .collect(toMap(PostImage::getPostId, PostImage::getObjectKey, (a, b) -> a));

        // 합치기 (postList 순서 = 랭킹)
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
                            return LoadUserPickTopBookmarkPostsVo.of(
                                    ranking,
                                    p,
                                    PropertiesHolder.CDN_PATH + "/" + imageUrl,
                                    toHashtagList(p.getHashtags()),
                                    userBookmarkMap.get(postId));
                        })
                .toList();
    }

    private List<String> toHashtagList(PostHashtag[] hashtags) {
        return (hashtags == null || hashtags.length == 0)
                ? List.of()
                : Arrays.stream(hashtags).map(PostHashtag::getTag).toList();
    }
}
