package com.ftm.server.adapter.out.scheduler;

import com.ftm.server.application.port.in.user.UserHardDeleteUseCase;
import com.ftm.server.common.annotation.Adapter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@Adapter
@RequiredArgsConstructor
public class UserHardDeleteScheduler {

    private final UserHardDeleteUseCase userHardDeleteUseCase;

    // 매일 새벽 3시 hard delete 진행
    @Scheduled(cron = "0 0 3 * * *")
    public void run() {
        userHardDeleteUseCase.execute();
    }
}
