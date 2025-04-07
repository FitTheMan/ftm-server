package com.ftm.server.application.vo.grooming;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class GroomingTestAvailabilityVo {

    private final boolean available;
    private final Long remainingDays;
    private final LocalDateTime lastTestedAt;
    private final LocalDate nextAvailableAt;

    private GroomingTestAvailabilityVo(
            boolean available,
            Long remainingDays,
            LocalDateTime lastTestedAt,
            LocalDate nextAvailableAt) {
        this.available = available;
        this.remainingDays = remainingDays;
        this.lastTestedAt = lastTestedAt;
        this.nextAvailableAt = nextAvailableAt;
    }

    public static GroomingTestAvailabilityVo available(LocalDateTime lastTestedAt) {
        return new GroomingTestAvailabilityVo(true, null, lastTestedAt, null);
    }

    public static GroomingTestAvailabilityVo unavailable(
            Long remainingDays, LocalDateTime lastTestedAt, LocalDate nextAvailableAt) {
        return new GroomingTestAvailabilityVo(false, remainingDays, lastTestedAt, nextAvailableAt);
    }
}
