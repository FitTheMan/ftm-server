package com.ftm.server.adapter.out.scheduler;

import com.ftm.server.application.port.out.cache.LoadUserPickTopBookmarkPostsWithCachePort;
import com.ftm.server.common.annotation.Adapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class GetUserPickTopBookmarkPostsScheduler {

    private final LoadUserPickTopBookmarkPostsWithCachePort cachePort;

    // cache TTL 1시간 만료 전에 주기적으로 58분 간격으로 갱신
    @Scheduled(fixedRateString = "PT58M", initialDelayString = "PT1M")
    public void run() {
        log.info(
                "Loading UserPick Top Bookmark Posts at {}",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        cachePort.getTopBookmarkPostsCachePut();
    }
}
