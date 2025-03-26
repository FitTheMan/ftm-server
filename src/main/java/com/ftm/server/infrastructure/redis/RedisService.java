package com.ftm.server.infrastructure.redis;

import com.ftm.server.application.port.RedisCachePort;
import com.ftm.server.application.port.RedisSessionPort;
import com.ftm.server.common.annotation.InfraService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

/** Redis Caching, Session 구현체 각 역할 별 레디스 조작 관리 (비즈니스 로직이 포함되면 안됨, 기술적인 로직만 수행) */
@InfraService
@RequiredArgsConstructor
public class RedisService implements RedisCachePort, RedisSessionPort {

    private final RedisTemplate<String, Object> redisTemplate;
}
