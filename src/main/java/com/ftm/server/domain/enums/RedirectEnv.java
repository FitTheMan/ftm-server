package com.ftm.server.domain.enums;

import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import java.util.Locale;

public enum RedirectEnv {
    TEST,
    LOCAL,
    DEV;

    public static RedirectEnv from(String value) {
        if (value == null || value.isBlank()) {
            return LOCAL;
        }

        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "test" -> TEST;
            case "local" -> LOCAL;
            case "dev" -> DEV;
            default -> throw new CustomException(ErrorResponseCode.INVALID_REDIRECT_ENV);
        };
    }

    public String key() {
        return name().toLowerCase();
    }
}
