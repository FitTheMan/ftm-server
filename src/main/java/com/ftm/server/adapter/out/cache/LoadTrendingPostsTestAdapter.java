package com.ftm.server.adapter.out.cache;

import com.ftm.server.application.port.out.cache.LoadTrendingPostsWithCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostImagePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostWithBookmarkCountPort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindPostsByCreatedDateQuery;
import com.ftm.server.application.vo.post.PostWithBookmarkCountVo;
import com.ftm.server.application.vo.post.TrendingPostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Primary
public class LoadTrendingPostsTestAdapter implements LoadTrendingPostsWithCachePort {

    private final LoadPostWithBookmarkCountPort loadPostPort;
    private final LoadPostImagePort loadPostImagePort;

    private static final int N = 15;

    @Override
    public List<TrendingPostVo> loadTrendingPosts() {

        // 현재보다 1주일 이전에 작성된 게시물만 조회(북마크 조회수 포함) (예전 게시물은 포함x)
        List<PostWithBookmarkCountVo> rawPosts =
                loadPostPort.loadAllPostsWithBookmarkCount(
                        FindPostsByCreatedDateQuery.of(LocalDate.now()));

        if (rawPosts.isEmpty()) return List.of();

        // 1. 최대값 계산 (정규화를 위해)
        long maxView = 1, maxLike = 1, maxScrap = 1;
        for (PostWithBookmarkCountVo post : rawPosts) {
            maxView = Math.max(maxView, post.getViewCount());
            maxLike = Math.max(maxLike, post.getLikeCount());
            maxScrap = Math.max(maxScrap, post.getScrapCount());
        }

        // 2. 점수 계산 후 정렬
        long finalMaxView = maxView;
        long finalMaxLike = maxLike;
        long finalMaxScrap = maxScrap;
        List<PostWithBookmarkCountVo> topN =
                rawPosts.stream()
                        .sorted(
                                Comparator.comparingDouble(
                                        p ->
                                                -((double) p.getViewCount() / finalMaxView
                                                        + (double) p.getLikeCount()
                                                        / finalMaxLike
                                                        + (double) p.getScrapCount()
                                                        / finalMaxScrap)
                                                        / 3.0))
                        .limit(N)
                        .toList();

        // 3. 각 게시물 이미지 조회 (없으면 null)
        List<Long> postIds = topN.stream().map(PostWithBookmarkCountVo::getPostId).toList();

        Map<Long, String> imageMap = new HashMap<>();
        postIds.forEach(p -> imageMap.put(p, null));
        loadPostImagePort
                .loadRepresentativeImagesByPostIds(FindByIdsQuery.from(postIds))
                .forEach(
                        postImage -> imageMap.put(postImage.getPostId(), postImage.getObjectKey()));

        // 4. 결과 조합 (순위 부여)
        return IntStream.range(0, topN.size())
                .mapToObj(
                        i -> {
                            PostWithBookmarkCountVo post = topN.get(i);
                            String imageKey = imageMap.get(post.getPostId());
                            return TrendingPostVo.from(post, i + 1, imageKey); // rank = i+1
                        })
                .toList();
    }
}
