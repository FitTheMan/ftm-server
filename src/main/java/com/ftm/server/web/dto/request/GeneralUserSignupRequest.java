package com.ftm.server.web.dto.request;

import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Data;

@Data
public class GeneralUserSignupRequest {

    @Pattern(
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "이메일 형식이 올바르지 않습니다.")
    private final String email;

    @NotBlank private final String password;

    @NotNull private final AgeGroup age;

    private final List<HashTag> hashtags;
}
