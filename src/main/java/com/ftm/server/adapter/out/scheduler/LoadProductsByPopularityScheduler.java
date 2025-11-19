package com.ftm.server.adapter.out.scheduler;

import com.ftm.server.application.port.out.cache.LoadProductsByPopularityCachePort;
import com.ftm.server.common.annotation.Adapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class LoadProductsByPopularityScheduler {

    private final LoadProductsByPopularityCachePort loadProductsByPopularityCachePort;

    // 4분 마다 캐시값 갱신 - cache put
    @Scheduled(fixedRateString = "PT4M", initialDelayString = "PT1M")
    public void loadProductsByPopularity() {
        loadProductsByPopularityCachePort.updateProductsByPopularity();
    }
}
