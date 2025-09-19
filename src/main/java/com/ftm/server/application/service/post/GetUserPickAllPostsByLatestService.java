package com.ftm.server.application.service.post;

import static java.util.stream.Collectors.toMap;

import com.ftm.server.application.port.in.post.GetUserPickPostsUseCase;
import com.ftm.server.application.port.out.persistence.post.LoadPostImagePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostPort;
import com.ftm.server.application.port.out.persistence.post.LoadPostWithBookmarkCountPort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindUserPickLatestPostsByCursorQuery;
import com.ftm.server.application.vo.post.*;
import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import com.ftm.server.domain.enums.PostHashtag;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserPickAllPostsByLatestService implements GetUserPickPostsUseCase {

    private final LoadPostPort loadPostPort;
    private final LoadPostWithBookmarkCountPort loadPostWithBookmarkCountPort;
    private final LoadPostImagePort loadPostImagePort;

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
