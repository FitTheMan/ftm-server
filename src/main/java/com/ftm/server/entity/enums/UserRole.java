package com.ftm.server.entity.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("admin"),
    USER("일반 user"),
    GUEST("방문자");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }
}
