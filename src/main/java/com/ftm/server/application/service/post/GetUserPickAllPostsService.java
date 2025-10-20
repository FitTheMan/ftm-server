package com.ftm.server.application.service.post;

import static java.util.stream.Collectors.toMap;

import com.ftm.server.application.port.in.post.GetUserPickPostsUseCase;
import com.ftm.server.application.port.out.cache.LoadUserPickAllPopularCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostImagePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostPort;
import com.ftm.server.application.port.out.persistence.post.LoadPostWithBookmarkCountPort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindByPostIdsAndUserQuery;
import com.ftm.server.application.query.FindUserPickLatestPostsByCursorQuery;
import com.ftm.server.application.query.FindUserPickPopularPostsByCursorQuery;
import com.ftm.server.application.vo.post.*;
import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import com.ftm.server.domain.enums.PostHashtag;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserPickAllPostsService implements GetUserPickPostsUseCase {

    private final LoadPostPort loadPostPort;
    private final LoadPostWithBookmarkCountPort loadPostWithBookmarkCountPort;
    private final LoadPostImagePort loadPostImagePort;
    private final LoadUserPickAllPopularCachePort loadUserPickAllPopularCachePort;

    @Override
    public GetUserPickAllPostsLatestWithCursorVo executeLatest(
            FindUserPickLatestPostsByCursorQuery query) {

        List<BookmarkYnWrapperVo> postsWithBookmarkYn =
                loadPostPort.loadUserPickAllPostsByLatest(query); // 최신순으로 조회. 마지막 cursor 위치를 기준으로

        if (postsWithBookmarkYn.isEmpty()) {
            return GetUserPickAllPostsLatestWithCursorVo.of(List.of(), false, null);
        }

        boolean hasNext = false;
        LocalDateTime lastCreatedAt = null;
        if (postsWithBookmarkYn.size() > query.getLimit()) {
            hasNext = true;
            postsWithBookmarkYn = postsWithBookmarkYn.subList(0, query.getLimit());
            lastCreatedAt =
                    ((Post) postsWithBookmarkYn.get(query.getLimit() - 1).getData()).getCreatedAt();
        }

        List<GetUserPickPostsVo> result = convertToVo(postsWithBookmarkYn.stream().toList());

        return GetUserPickAllPostsLatestWithCursorVo.of(result, hasNext, lastCreatedAt);
    }

    @Override
    public GetUserPickAllPostsPopularWithCursorVo executePopular(
            FindUserPickPopularPostsByCursorQuery query) {
        List<UserPickPopularPostCursorVo> allPosts =
                loadUserPickAllPopularCachePort.getUserPickAllPopularPosts();

        // Step 1. 커서 조건 적용 (lastScore, lastId 기준)
        List<UserPickPopularPostCursorVo> filteredPosts = applyCursorFilter(allPosts, query);

        // Step 2. 인기 점수 기준 내림차순 정렬 + 페이징(limit + 1)
        List<UserPickPopularPostCursorVo> sortedAndPaged =
                filteredPosts.stream()
                        .sorted(
                                Comparator.comparingDouble(UserPickPopularPostCursorVo::getScore)
                                        .reversed())
                        .limit(query.getSize() + 1)
                        .toList();

        // Step 3. 다음 페이지 여부 판단
        boolean hasNext = sortedAndPaged.size() > query.getSize();
        if (hasNext) {
            sortedAndPaged = sortedAndPaged.subList(0, query.getSize());
        }

        // Step 4. 다음 커서 계산
        Double nextScore = hasNext ? getLast(sortedAndPaged).getScore() : null;
        Long nextId = hasNext ? getLast(sortedAndPaged).getPostId() : null;

        // Step 5. 포스트 상세 조회 + 북마크 여부 매핑
        List<Long> postIds =
                sortedAndPaged.stream().map(UserPickPopularPostCursorVo::getPostId).toList();

        List<PostIdAndBookmarkYnVo> bookmarkInfoList =
                resolveBookmarkInfo(postIds, query.getUserId());

        // Step 6. 응답 변환
        List<GetUserPickPostsVo> posts = convertToVo2(bookmarkInfoList);

        return GetUserPickAllPostsPopularWithCursorVo.of(posts, hasNext, nextId, nextScore);
    }

    /** 커서 기반 필터링 수행 */
    private List<UserPickPopularPostCursorVo> applyCursorFilter(
            List<UserPickPopularPostCursorVo> posts, FindUserPickPopularPostsByCursorQuery query) {

        Long lastId = query.getLastId();
        Double lastScore = query.getLastScore();

        if (lastId == null || lastScore == null) {
            return posts;
        }

        return posts.stream()
                .filter(
                        p ->
                                p.getScore() < lastScore
                                        || (p.getScore().equals(lastScore)
                                                && p.getPostId() < lastId))
                .toList();
    }

    /** userId 존재 여부에 따라 북마크 정보 로드 */
    private List<PostIdAndBookmarkYnVo> resolveBookmarkInfo(List<Long> postIds, Long userId) {
        if (userId == null) {
            return postIds.stream().map(id -> new PostIdAndBookmarkYnVo(id, false)).toList();
        }
        // DB에서 가져온 결과 (순서 보장 안됨)
        List<PostIdAndBookmarkYnVo> unorderedList =
                loadPostPort.loadPostIdAndBookmarkYn(FindByPostIdsAndUserQuery.of(postIds, userId));

        // postId -> VO 맵핑
        Map<Long, PostIdAndBookmarkYnVo> map =
                unorderedList.stream()
                        .collect(Collectors.toMap(PostIdAndBookmarkYnVo::getPostId, vo -> vo));

        // 원래 postIds 순서대로 정렬
        return postIds.stream().map(map::get).toList();
    }

    /** 북마크 및 포스트 상세 데이터를 통합하여 응답 VO로 변환 */
    private List<GetUserPickPostsVo> convertToVo2(List<PostIdAndBookmarkYnVo> posts) {
        List<Long> postIds = posts.stream().map(PostIdAndBookmarkYnVo::getPostId).toList();

        // Post 상세 정보
        Map<Long, PostWithUserAndBookmarkCountVo> postDetailMap =
                loadPostWithBookmarkCountPort
                        .loadPostWithUserAndBookmarkCount(FindByIdsQuery.from(postIds))
                        .stream()
                        .collect(Collectors.toMap(PostWithUserAndBookmarkCountVo::getId, vo -> vo));

        // 대표 이미지
        Map<Long, String> imageMap =
                loadPostImagePort
                        .loadRepresentativeImagesByPostIds(FindByIdsQuery.from(postIds))
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        PostImage::getPostId,
                                        PostImage::getObjectKey,
                                        (a, b) -> a));

        return posts.stream()
                .map(
                        b -> {
                            PostWithUserAndBookmarkCountVo post = postDetailMap.get(b.getPostId());
                            String imageUrl =
                                    imageMap.getOrDefault(
                                            post.getId(), PropertiesHolder.POST_DEFAULT_IMAGE);
                            List<String> hashtags = toHashtagList(post.getHashtags());

                            return GetUserPickPostsVo.of(
                                    post,
                                    PropertiesHolder.CDN_PATH + "/" + imageUrl,
                                    hashtags,
                                    b.getBookmarkYn());
                        })
                .toList();
    }

    /** 리스트의 마지막 요소를 안전하게 반환 */
    private <T> T getLast(List<T> list) {
        return list.get(list.size() - 1);
    }

    private List<GetUserPickPostsVo> convertToVo(List<BookmarkYnWrapperVo> posts) {

        List<Long> postIds = posts.stream().map(b -> ((Post) b.getData()).getId()).toList();

        Map<Long, PostWithUserAndBookmarkCountVo> detailPostMap =
                loadPostWithBookmarkCountPort
                        .loadPostWithUserAndBookmarkCount(FindByIdsQuery.from(postIds))
                        .stream()
                        .collect(toMap(PostWithUserAndBookmarkCountVo::getId, vo -> vo));

        Map<Long, String> imageUrlMap =
                loadPostImagePort
                        .loadRepresentativeImagesByPostIds(FindByIdsQuery.from(postIds))
                        .stream()
                        .collect(toMap(PostImage::getPostId, PostImage::getObjectKey, (a, b) -> a));

        return posts.stream()
                .map(
                        b -> {
                            PostWithUserAndBookmarkCountVo p =
                                    detailPostMap.get(((Post) b.getData()).getId());
                            String imageUrl =
                                    imageUrlMap.getOrDefault(
                                            p.getId(), PropertiesHolder.POST_DEFAULT_IMAGE);
                            List<String> hashtags = toHashtagList(p.getHashtags());

                            return GetUserPickPostsVo.of(
                                    p,
                                    PropertiesHolder.CDN_PATH + "/" + imageUrl,
                                    hashtags,
                                    b.getBookmarkYn());
                        })
                .toList();
    }

    private List<String> toHashtagList(PostHashtag[] hashtags) {
        return (hashtags == null || hashtags.length == 0)
                ? List.of()
                : Arrays.stream(hashtags).map(PostHashtag::getTag).toList();
    }
}
