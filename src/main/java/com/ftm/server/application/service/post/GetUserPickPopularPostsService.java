package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.user.GetUserPickPopularPostsUseCase;
import com.ftm.server.application.port.out.cache.LoadUserPickPopularWithCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostImagePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostWithBookmarkCountPort;
import com.ftm.server.application.port.out.persistence.post.LoadUserForPostDomainPort;
import com.ftm.server.application.query.FindBookmarkCountByPostIdsQuery;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.vo.post.PostAndBookmarkCountVo;
import com.ftm.server.application.vo.post.UserIdAndNameVo;
import com.ftm.server.application.vo.post.UserPickPopularPostsVo;
import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import com.ftm.server.domain.enums.PostHashtag;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserPickPopularPostsService implements GetUserPickPopularPostsUseCase {

    private final LoadUserPickPopularWithCachePort loadUserPickPopularWithCachePort;

    private final LoadPostWithBookmarkCountPort loadPostWithBookmarkCountPort;
    private final LoadPostImagePort loadPostImagePort;
    private final LoadUserForPostDomainPort loadUserForPostDomainPort;

    @Override
    public List<UserPickPopularPostsVo> execute() {

        // 1) 상위 4개 게시물의 id 를 cache 에서 조회
        List<Post> postList = loadUserPickPopularWithCachePort.getUserPickPopularPost();

        // 2) id 목록
        List<Long> postIds = postList.stream().map(Post::getId).toList();
        List<Long> authorIds = postList.stream().map(Post::getUserId).toList();

        // 3) 북마크(스크랩) 수
        List<PostAndBookmarkCountVo> postAndBookmarkCountVos =
                loadPostWithBookmarkCountPort.getPostAndBookmarkCount(
                        FindBookmarkCountByPostIdsQuery.of(postIds));

        var scrapCountMap =
                postAndBookmarkCountVos.stream()
                        .collect(
                                java.util.stream.Collectors.toMap(
                                        PostAndBookmarkCountVo::getPostId,
                                        PostAndBookmarkCountVo::getBookmarkCount));

        // 4) 작성자 이름
        List<UserIdAndNameVo> userIdAndNameVos =
                loadUserForPostDomainPort.loadPostAndAuthorName(FindByIdsQuery.from(authorIds));
        var authorNameMap =
                userIdAndNameVos.stream()
                        .collect(
                                java.util.stream.Collectors.toMap(
                                        UserIdAndNameVo::getUserId,
                                        UserIdAndNameVo::getAuthorName,
                                        (a, b) -> a // 중복 authorId 있을 경우 첫 값 사용
                                        ));

        // 5) 대표 이미지
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

        // 6) 합치기 (postList 순서 = 랭킹)
        return IntStream.range(0, postList.size())
                .mapToObj(
                        i -> {
                            Post p = postList.get(i);
                            int ranking = i + 1;

                            long scrapCount = scrapCountMap.getOrDefault(p.getId(), 0L);
                            String authorName = authorNameMap.getOrDefault(p.getUserId(), "");
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
                            return UserPickPopularPostsVo.of(
                                    ranking,
                                    p,
                                    authorName,
                                    scrapCount,
                                    PropertiesHolder.CDN_PATH + "/" + imageUrl,
                                    hashtags);
                        })
                .toList();
    }
}
