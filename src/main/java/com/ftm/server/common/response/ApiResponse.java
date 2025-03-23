package com.ftm.server.common.response;

import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private final Integer status;
    private final String code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success(SuccessResponseCode responseCode) {
        return success(responseCode, null);
    }

    public static <T> ApiResponse<T> success(SuccessResponseCode responseCode, T data) {
        return new ApiResponse<>(
                responseCode.getHttpStatus().value(),
                responseCode.getCode(),
                responseCode.getMessage(),
                data);
    }

    public static <T> ApiResponse<T> fail(ErrorResponseCode responseCode) {
        return fail(responseCode, null);
    }

    public static <T> ApiResponse<T> fail(ErrorResponseCode responseCode, T data) {
        return new ApiResponse<>(
                responseCode.getHttpStatus().value(),
                responseCode.getCode(),
                responseCode.getMessage(),
                data);
    }
}
