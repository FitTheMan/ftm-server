package com.ftm.server.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorResponseCode {

    // 400번
    INVALID_REQUEST_ARGUMENT(
            HttpStatus.BAD_REQUEST, "E400_001", "클라이언트 요청값의 일부가 잘못된 형식이거나, 필수 데이터가 누락되었습니다."),

    // 401번
    NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "E401_001", "인증되지 않은 사용자입니다."),

    // 403번
    NOT_AUTHORIZATION(HttpStatus.FORBIDDEN, "E403_001", "인증된 사용자이나 해당 자원에 대한 접근 권한이 없습니다."),

    // 404번
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_001", "요청된 사용자를 찾을 수 없습니다."),

    // 409번
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "E409_001", "이미 존재하는 사용자입니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.CONFLICT, "E409_002", "비밀번호가 일치하지 않습니다."),

    // 500번
    UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500_001", "알 수 없는 서버 에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorResponseCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
