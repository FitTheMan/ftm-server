package com.ftm.server.adapter.out.scheduler;

import com.ftm.server.application.port.out.cache.LoadUserPickBiblePostsWithCachePort;
import com.ftm.server.common.annotation.Adapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class GetUserPickBiblePostsScheduler {

    private final LoadUserPickBiblePostsWithCachePort loadUserPickBiblePostsWithCachePort;

    // 마지막 실행으로부터 57분 뒤에 재실행됨.
    // cache TTL 값인 1시간이 끝나기 이전에 캐시를 업데이트 해 놓는다.
    @Scheduled(fixedRateString = "PT57M", initialDelayString = "PT1M")
    public void run() {
        log.info(
                "Loading UserPick Bible Posts at {}",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        loadUserPickBiblePostsWithCachePort.getUserPickBiblePostCachePut();
    }
}
