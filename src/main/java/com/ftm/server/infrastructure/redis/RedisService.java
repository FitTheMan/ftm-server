package com.ftm.server.infrastructure.redis;

import com.ftm.server.adapter.gateway.RedisCacheGateway;
import com.ftm.server.adapter.gateway.RedisSessionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/** Redis Caching, Session 구현체 각 역할 별 레디스 조작 관리 (비즈니스 로직이 포함되면 안됨, 기술적인 로직만 수행) */
@Service
@RequiredArgsConstructor
public class RedisService implements RedisCacheGateway, RedisSessionGateway {

    private final RedisTemplate<String, Object> redisTemplate;
}
