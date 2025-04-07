package com.ftm.server.application.service.grooming;

import static com.ftm.server.common.consts.StaticConsts.MINIMUM_DAYS_BETWEEN_TESTS;

import com.ftm.server.application.port.in.grooming.CheckGroomingTestAvailabilityUseCase;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingTestResultPort;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.grooming.GroomingTestAvailabilityVo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckGroomingTestAvailabilityService implements CheckGroomingTestAvailabilityUseCase {

    private final LoadGroomingTestResultPort loadGroomingTestResultPort;

    @Override
    public GroomingTestAvailabilityVo execute(FindByUserIdQuery query) {
        // 가장 최근 테스트 날짜 조회
        LocalDateTime lastTestDateTime =
                loadGroomingTestResultPort.loadLatestTestedAtByUserId(query);
        if (lastTestDateTime == null) { // 테스트 기록이 없는 경우
            return GroomingTestAvailabilityVo.available(null);
        }

        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate lastTestDate = lastTestDateTime.toLocalDate();

        // 가장 마지막에 수행한 테스트 날짜와 현재 날짜를 비교해 테스트 가능 여부 판단
        long daysSinceLastTest = ChronoUnit.DAYS.between(lastTestDate, now);
        if (daysSinceLastTest < MINIMUM_DAYS_BETWEEN_TESTS) { // 마지막 테스트일로부터 7일이 지나지 않은 경우
            long remainingDays = MINIMUM_DAYS_BETWEEN_TESTS - daysSinceLastTest;
            return GroomingTestAvailabilityVo.unavailable(
                    remainingDays,
                    lastTestDateTime,
                    lastTestDate.plusDays(MINIMUM_DAYS_BETWEEN_TESTS));
        }

        return GroomingTestAvailabilityVo.available(lastTestDateTime); // 마지막 테스트일로부터 7일이 지난 경우
    }
}
