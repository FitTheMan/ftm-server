package com.ftm.server.adapter.out.scheduler;

import com.ftm.server.application.port.out.cache.LoadUserPickPopularWithCachePort;
import com.ftm.server.common.annotation.Adapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Adapter
@Slf4j
@RequiredArgsConstructor
public class GetUserPickPopularPostsScheduler {

    private final LoadUserPickPopularWithCachePort loadUserPickPopularWithCachePort;

    // 마지막 실행으로부터 55분 뒤에 재실행됨.
    // cache TTL 값인 1시간이 끝나기 이전에 캐시를 업데이트 해 놓는다.
    @Scheduled(fixedRateString = "PT55M", initialDelayString = "PT1M")
    public void run() {
        log.info(
                "Loading UserPick Popular Posts at {}",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        loadUserPickPopularWithCachePort.getUserPickPopularPostCachePut();
    }
}
