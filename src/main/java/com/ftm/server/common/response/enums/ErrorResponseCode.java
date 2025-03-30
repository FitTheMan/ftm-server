package com.ftm.server.common.response.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorResponseCode {

    // 400번
    INVALID_REQUEST_ARGUMENT(
            HttpStatus.BAD_REQUEST, "E400_001", "클라이언트 요청값의 일부가 잘못된 형식이거나, 필수 데이터가 누락되었습니다."),
    INVALID_SEESION_FOR_SOCIAL_USER_SIGNUP(
            HttpStatus.BAD_REQUEST, "E400_002", "소셜 회원가입을 위한 session 값이 잘못됨"),

    // 401번
    NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "E401_001", "인증되지 않은 사용자입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "E401_002", "인증에 실패하였습니다."),

    // 403번
    NOT_AUTHORIZATION(HttpStatus.FORBIDDEN, "E403_001", "인증된 사용자이나 해당 자원에 대한 접근 권한이 없습니다."),

    // 404번
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_001", "요청된 사용자를 찾을 수 없습니다."),
    USER_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_002", "요청한 유저 이미지를 찾을 수 없습니다."),

    // 409번
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "E409_001", "이미 존재하는 사용자입니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.CONFLICT, "E409_002", "비밀번호가 일치하지 않습니다."),
    EXCEED_NUMBER_OF_TRIAL(HttpStatus.CONFLICT, "E409_003", "시도 가능 횟수를 초과했습니다. 잠시 후에 다시 시도 해 주세요."),
    EMAIL_NOT_VERIFIED(HttpStatus.CONFLICT, "E409_004", "이메일 인증이 완료되지 않았습니다."),

    // 500번
    UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500_001", "알 수 없는 서버 에러가 발생했습니다."),
    FAIL_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E500_002", "서버 내부 문제로 메일 전송에 실패했습니다."),

    // 502번 (외부 서비스에서 문제 발생)
    KAKAO_AUTH_TOKEN_EXCHANGE_FAILED(HttpStatus.BAD_GATEWAY, "E502_001", "카카오 인증 토큰 요청에 실패했습니다."),
    KAKAO_USER_PROFILE_FETCH_FAILED(HttpStatus.BAD_GATEWAY, "E502_002", "카카오 사용자 정보 요청에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorResponseCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
