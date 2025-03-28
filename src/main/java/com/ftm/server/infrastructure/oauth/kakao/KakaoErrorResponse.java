package com.ftm.server.infrastructure.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoErrorResponse {

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}
