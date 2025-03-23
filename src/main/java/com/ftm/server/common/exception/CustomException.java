package com.ftm.server.common.exception;

import com.ftm.server.common.response.enums.ErrorResponseCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorResponseCode errorResponseCode;

    public CustomException(ErrorResponseCode errorResponseCode, String message) {
        super(message); // 예외 메시지 설정
        this.errorResponseCode = errorResponseCode;
    }

    public CustomException(ErrorResponseCode errorResponseCode) {
        super(errorResponseCode.getMessage()); // 예외 메시지 설정
        this.errorResponseCode = errorResponseCode;
    }

    public static CustomException USER_NOT_FOUND =
            new CustomException(ErrorResponseCode.USER_NOT_FOUND);

    public static CustomException USER_ALREADY_EXISTS =
            new CustomException(ErrorResponseCode.USER_ALREADY_EXISTS);
}
