package com.ftm.server.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessResponseCode {
    OK(HttpStatus.OK, "S001", "클라이언트 요청이 정상적으로 처리됨."),
    CREATED(HttpStatus.CREATED, "S002", "새로운 리소스가 성공적으로 생성됨."),
    ACCEPTED(HttpStatus.ACCEPTED, "S003", "요청은 수락되었지만 아직 처리가 완료되지 않음."),
    NO_CONTENT(HttpStatus.NO_CONTENT, "S004", "요청이 성공적으로 처리되었지만, 응답 본문이 필요 없음.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    SuccessResponseCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
