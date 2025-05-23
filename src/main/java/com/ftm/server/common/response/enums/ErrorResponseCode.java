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
    INVALID_MAX_SCORE(HttpStatus.BAD_REQUEST, "E400_003", "최대 점수 값은 0보다 커야합니다."),
    INVALID_RATIO_FOR_PERCENTAGE(HttpStatus.BAD_REQUEST, "E400_004", "퍼센트 계산 오류, 잘못된 ratio 값입니다."),
    INVALID_GROOMING_TEST_QUESTION_ID(
            HttpStatus.BAD_REQUEST, "E400_005", "유효하지 않은 그루밍 테스트 질문 정보입니다."),
    INVALID_GROOMING_TEST_ANSWER_ID(
            HttpStatus.BAD_REQUEST, "E400_006", "유효하지 않은 그루밍 테스트 답변 정보입니다."),
    INVALID_IMAGE_FORMAT(
            HttpStatus.BAD_REQUEST, "E400_007", "유효하지 않은 이미지입니다. 포맷, 크기(최대 10MB), 존재 유무를 확인해 주세요."),
    INVALID_POST_PRODUCT_IMAGE_MAPPING(
            HttpStatus.BAD_REQUEST,
            "E400_008",
            "상품과 이미지 간의 매핑이 올바르지 않습니다. imageIndex와 이미지 수를 확인해주세요."),
    CANNOT_DELETE_DEFAULT_IMAGE(HttpStatus.BAD_REQUEST, "E400_009", "기본 이미지는 삭제할 수 없습니다."),
    POST_PRODUCT_IMAGE_ALREADY_EXISTS(
            HttpStatus.BAD_REQUEST, "E400_010", "이미 이미지가 존재합니다. 기존 이미지를 삭제하고 업로드 해주세요."),

    BAD_REQUEST_PAGING_INDEX_RANGE(HttpStatus.BAD_REQUEST, "E400_011", "페이지 번호는 최소 0 이상이여야 합니다."),
    BAD_REQUEST_PAGING_SIZE_RANGE(
            HttpStatus.BAD_REQUEST, "E400_012", "페이지당 개수는 최소 1 이상, 10 이하여야 합니다."),

    // 401번
    NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "E401_001", "인증되지 않은 사용자입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "E401_002", "인증에 실패하였습니다."),

    // 403번
    NOT_AUTHORIZATION(HttpStatus.FORBIDDEN, "E403_001", "인증된 사용자이나 해당 자원에 대한 접근 권한이 없습니다."),
    UNAUTHORIZED_POST_ACCESS(HttpStatus.FORBIDDEN, "E403_002", "해당 게시글에 대한 권한이 없습니다."),
    UNAUTHORIZED_POST_IMAGE_ACCESS(HttpStatus.FORBIDDEN, "E403_003", "해당 게시글 이미지에 대한 권한이 없습니다."),
    UNAUTHORIZED_POST_PRODUCT_ACCESS(HttpStatus.FORBIDDEN, "E403_004", "해당 상품에 대한 권한이 없습니다."),
    UNAUTHORIZED_POST_PRODUCT_IMAGE_ACCESS(
            HttpStatus.FORBIDDEN, "E403_005", "해당 상품 이미지에 대한 권한이 없습니다."),

    // 404번
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_001", "요청된 사용자를 찾을 수 없습니다."),
    USER_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_002", "요청한 유저 이미지를 찾을 수 없습니다."),
    GROOMING_LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_003", "요청한 그루밍 레벨 정보를 찾을 수 없습니다."),
    GROOMING_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_004", "요청한 그루밍 카테고리 정보를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_005", "요청한 게시글을 찾을 수 없습니다."),
    POST_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_006", "요청한 상품을 찾을 수 없습니다."),
    POST_PRODUCT_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_007", "요청한 상품 이미지를 찾을 수 없습니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "E404_008", "요청된 사용자와 게시글에 부합하는 북마크를 찾을 수 없습니다."),
    GROOMING_TEST_QUESTION_NOT_FOUND(
            HttpStatus.NOT_FOUND, "E404_009", "요청한 그루밍 테스트 질문 정보를 찾을 수 없습니다."),
    GROOMING_TEST_ANSWER_NOT_FOUND(
            HttpStatus.NOT_FOUND, "E404_010", "요청한 그루밍 테스트 답변 정보를 찾을 수 없습니다."),

    // 409번
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "E409_001", "이미 존재하는 사용자입니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.CONFLICT, "E409_002", "비밀번호가 일치하지 않습니다."),
    EXCEED_NUMBER_OF_TRIAL(HttpStatus.CONFLICT, "E409_003", "시도 가능 횟수를 초과했습니다. 잠시 후에 다시 시도 해 주세요."),
    EMAIL_NOT_VERIFIED(HttpStatus.CONFLICT, "E409_004", "이메일 인증이 완료되지 않았습니다."),

    // 500번
    UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500_001", "알 수 없는 서버 에러가 발생했습니다."),
    FAIL_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "E500_002", "서버 내부 문제로 메일 전송에 실패했습니다."),
    FAIL_TO_UPLOAD_IMAGE(
            HttpStatus.INTERNAL_SERVER_ERROR, "E500_003", "알 수 없는 이유로 이미지 업로드에 실패했습니다."),

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
