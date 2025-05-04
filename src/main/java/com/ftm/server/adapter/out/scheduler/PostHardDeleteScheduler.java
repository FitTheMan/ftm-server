package com.ftm.server.adapter.out.scheduler;

import com.ftm.server.application.port.in.post.PostHardDeleteUseCase;
import com.ftm.server.common.annotation.Adapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class PostHardDeleteScheduler {

    private final PostHardDeleteUseCase postHardDeleteUseCase;

    // 매일 오전 3시 hard delete 진행
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void run() {
        log.info(
                "Posts Hard Delete started at {}",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        postHardDeleteUseCase.execute();
        log.info("Posts Hard Deleted Finish.");
    }
}
