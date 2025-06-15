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
}
