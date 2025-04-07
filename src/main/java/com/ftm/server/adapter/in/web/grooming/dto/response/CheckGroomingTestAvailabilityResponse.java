package com.ftm.server.adapter.in.web.grooming.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftm.server.application.vo.grooming.GroomingTestAvailabilityVo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CheckGroomingTestAvailabilityResponse {

    private final boolean available;
    private final Long remainingDays;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private final LocalDateTime lastTestedAt;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private final LocalDate nextAvailableAt;

    private CheckGroomingTestAvailabilityResponse(
            GroomingTestAvailabilityVo groomingTestAvailabilityVo) {
        this.available = groomingTestAvailabilityVo.isAvailable();
        this.remainingDays = groomingTestAvailabilityVo.getRemainingDays();
        this.lastTestedAt = groomingTestAvailabilityVo.getLastTestedAt();
        this.nextAvailableAt = groomingTestAvailabilityVo.getNextAvailableAt();
    }

    public static CheckGroomingTestAvailabilityResponse from(
            GroomingTestAvailabilityVo groomingTestAvailabilityVo) {
        return new CheckGroomingTestAvailabilityResponse(groomingTestAvailabilityVo);
    }
}
