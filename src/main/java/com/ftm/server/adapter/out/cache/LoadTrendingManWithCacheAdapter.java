package com.ftm.server.adapter.out.cache;

import com.ftm.server.application.port.out.cache.LoadTrendingManWithCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostWithBookmarkCountPort;
import com.ftm.server.application.port.out.persistence.user.LoadUserImagePort;
import com.ftm.server.application.query.FindPostsByCreatedDateQuery;
import com.ftm.server.application.query.FindUserImagesByIdsQuery;
import com.ftm.server.application.vo.post.TrendingUserVo;
import com.ftm.server.application.vo.post.UserWithPostCountVo;
import com.ftm.server.common.annotation.Adapter;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;

@Adapter
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheManager = "trendingUsersCacheManager")
public class LoadTrendingManWithCacheAdapter implements LoadTrendingManWithCachePort {

    private final LoadPostWithBookmarkCountPort loadPostPort;

    private final LoadUserImagePort loadUserImagePort;

    private final Integer N = 5;

    @Override
    public List<TrendingUserVo> loadTrendingMan() {

        // 현재보다 1주일 이전에 작성된 게시물만 조회(북마크 조회수 포함) (예전 게시물은 포함x)
        List<UserWithPostCountVo> userWithCount =
                loadPostPort.loadAllPostsWithUserAndBookmarkCount(
                        FindPostsByCreatedDateQuery.of(LocalDate.now()));

        // 1. 최대값 계산 (정규화를 위해)
        long maxView = 1, maxLike = 1, maxScrap = 1;
        for (UserWithPostCountVo post : userWithCount) {
            maxView = Math.max(maxView, post.getViewCount());
            maxLike = Math.max(maxLike, post.getLikeCount());
            maxScrap = Math.max(maxScrap, post.getScrapCount());
        }

        // 2. 점수 계산 후 정렬
        long finalMaxView = maxView;
        long finalMaxLike = maxLike;
        long finalMaxScrap = maxScrap;
        List<UserWithPostCountVo> topN =
                userWithCount.stream()
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

        // 3. 각 User 이미지 조회
        List<Long> userIds = topN.stream().map(UserWithPostCountVo::getUserId).toList();

        Map<Long, String> imageMap = new HashMap<>();
        userIds.forEach(p -> imageMap.put(p, null));
        loadUserImagePort
                .loadUserImagesByUserIdIn(FindUserImagesByIdsQuery.of(userIds))
                .forEach(
                        userImage -> imageMap.put(userImage.getUserId(), userImage.getObjectKey()));

        // 4. 결과 조합 (순위 부여)
        return IntStream.range(0, topN.size())
                .mapToObj(
                        i -> {
                            UserWithPostCountVo userWithPostCountVo = topN.get(i);
                            String imageKey = imageMap.get(userWithPostCountVo.getUserId());
                            return TrendingUserVo.of(
                                    i + 1,
                                    userWithPostCountVo.getUserId(),
                                    userWithPostCountVo.getUserName(),
                                    imageKey); // rank = i+1
                        })
                .toList();
    }
}
