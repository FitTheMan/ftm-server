package com.ftm.server.application.query;

import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public class FindByEmailQuery {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final String email;

    private FindByEmailQuery(String email) {
        if (!isValidEmail(email)) {
            throw new CustomException(
                    ErrorResponseCode.INVALID_REQUEST_ARGUMENT, "이메일 형식이 올바르지 않습니다.");
        }
        this.email = email;
    }

    public static FindByEmailQuery of(String email) {
        return new FindByEmailQuery(email);
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
