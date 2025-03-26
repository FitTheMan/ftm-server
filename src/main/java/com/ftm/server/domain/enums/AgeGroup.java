package com.ftm.server.domain.enums;

import lombok.Getter;

@Getter
public enum AgeGroup {
    TEENS("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    FIFTIES("50대");

    private final String value;

    AgeGroup(String value) {
        this.value = value;
    }
}
