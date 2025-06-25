package com.ftm.server.adapter.in.web.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecoverUserAccountRequest {

    @Pattern(
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "이메일 형식이 올바르지 않습니다.")
    private final String email;

    public static RecoverUserAccountRequest of(String email) {
        return new RecoverUserAccountRequest(email);
    }
}
