package com.ftm.server.application.query;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class FindGroomingTestResultByUserIdAndTestedAtQuery {

    private final Long userId;
    private final LocalDate testedAt;

    private FindGroomingTestResultByUserIdAndTestedAtQuery(Long userId, LocalDate testedAt) {
        this.userId = userId;
        this.testedAt = testedAt;
    }

    public static FindGroomingTestResultByUserIdAndTestedAtQuery of(
            Long userId, LocalDate testedAt) {
        return new FindGroomingTestResultByUserIdAndTestedAtQuery(userId, testedAt);
    }
}
