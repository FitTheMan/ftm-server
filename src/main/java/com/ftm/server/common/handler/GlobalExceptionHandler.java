package com.ftm.server.common.handler;

import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ApiResponse> handleCustomException(CustomException e) {
        log.error(
                "[{}] code:{} / code message:{}",
                e.getErrorResponseCode().name(),
                e.getErrorResponseCode().getCode(),
                e.getMessage());
        return ResponseEntity.status(e.getErrorResponseCode().getHttpStatus())
                .body(ApiResponse.fail(e.getErrorResponseCode()));
    }

    // 기타 처리되지 못한 exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlingException(Exception e) {

        log.error(
                "[Exception] code : {}  code message : {}",
                ErrorResponseCode.UNKNOWN_SERVER_ERROR.getCode(),
                e.getMessage());
        return ResponseEntity.status(ErrorResponseCode.UNKNOWN_SERVER_ERROR.getHttpStatus())
                .body(ApiResponse.fail(ErrorResponseCode.UNKNOWN_SERVER_ERROR));
    }

    // request body의 type이 잘못된 경우
    @ExceptionHandler({
        MethodArgumentNotValidException
                .class, // json body (requestpart의 body, requestBody의 body)의 필드가 설정한 유효값을 만족시키지 않거나,
        // 필수값이 누락됨.
        HttpMessageNotReadableException
                .class, // json body (requestpart의 body, requestBody의 body)의 필드 type이 잘못됨.
        MissingServletRequestPartException.class, // required인 requestpart가 없음.
        MissingServletRequestParameterException.class, // requried인 request param이 없음.
        MethodArgumentTypeMismatchException.class // request parameter, pathVariable의 type이 잘못됨.
    })
    public ResponseEntity<ApiResponse> handleMissingServletRequestPartException(Exception e) {

        log.error(
                "[Exception] code : {}  code message : {}",
                ErrorResponseCode.INVALID_REQUEST_ARGUMENT.getCode(),
                e.getMessage());
        return ResponseEntity.status(ErrorResponseCode.INVALID_REQUEST_ARGUMENT.getHttpStatus())
                .body(ApiResponse.fail(ErrorResponseCode.INVALID_REQUEST_ARGUMENT));
    }
}
