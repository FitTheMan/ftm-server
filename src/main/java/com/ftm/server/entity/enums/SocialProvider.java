package com.ftm.server.entity.enums;

import lombok.Getter;

@Getter
public enum SocialProvider {
    KAKAO("카카오");

    private final String value;

    SocialProvider(String value) {
        this.value = value;
    }
}
