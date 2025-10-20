package com.ftm.server.infrastructure.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@EnableCaching
@Configuration
public class CaffeineCacheConfig {

    @Bean
    @Primary
    public CaffeineCacheManager cacheManager() { // 기본 캐시 매니저(아무것도 지정하지 않았을 시)
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setAllowNullValues(false); // null 값 저장하지 않음
        manager.setCaffeine(Caffeine.newBuilder().maximumSize(100).recordStats());

        return manager;
    }

    @Bean("trendingPostsCacheManager") // 트렌딩 게시물 전용 캐시 매지너 TTL 설정 달리함 : 5분에 한번씩 캐시 무효화.
    public CaffeineCacheManager cacheManagerForTrendingPosts() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setAllowNullValues(false); // null 값 저장하지 않음
        manager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .maximumSize(10)
                        .recordStats());
        return manager;
    }

    @Bean("trendingUsersCacheManager") // 트렌딩 게시물 전용 캐시 매지너 TTL 설정 달리함 : 5분에 한번씩 캐시 무효화.
    public CaffeineCacheManager cacheManagerForTrendingUsers() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setAllowNullValues(false); // null 값 저장하지 않음
        manager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .maximumSize(10)
                        .recordStats());
        return manager;
    }

    @Bean("userPickPopularPostsCacheManager") // 유저픽 게시글 - 최근 인기있는 게시물 용 캐시 매니저
    public CaffeineCacheManager cacheManagerForUserPickPopularPosts() {
        CaffeineCacheManager mgr = new CaffeineCacheManager();
        mgr.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(10)
                        // 1시간에 한번 씩 최신 인기 게시물 갱신
                        .expireAfterWrite(1, TimeUnit.HOURS));
        return mgr;
    }

    @Bean("userPickTopBookmarkPostsCacheManager") // 유저픽 게시글 - 북마크 상위 게시물 용 캐시 매니저
    public CaffeineCacheManager cacheManagerForUserPickTopBookmarkPosts() {
        CaffeineCacheManager mgr = new CaffeineCacheManager();
        mgr.setCaffeine(Caffeine.newBuilder().maximumSize(10).expireAfterWrite(1, TimeUnit.HOURS));
        return mgr;
    }

    @Bean("userPickAllPopularPostsCacheManager") // 유저픽 게시글 - 그루밍 이야기 - 인기순 api
    public CaffeineCacheManager cacheManagerForUserPickAllPopularPosts() {
        CaffeineCacheManager mgr = new CaffeineCacheManager();
        mgr.setCaffeine(
                Caffeine.newBuilder().maximumSize(10).expireAfterWrite(2, TimeUnit.MINUTES));
        return mgr;
    }
}
