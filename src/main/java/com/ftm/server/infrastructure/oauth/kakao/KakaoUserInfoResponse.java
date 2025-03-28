package com.ftm.server.infrastructure.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoResponse {

    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    static class KakaoAccount {

        @JsonProperty("profile_needs_agreement")
        private Boolean profileNeedsAgreement;

        @JsonProperty("profile_nickname_needs_agreement")
        private Boolean profileNicknameNeedsAgreement;

        @JsonProperty("profile_image_needs_agreement")
        private Boolean profileImageNeedsAgreement;

        @JsonProperty("profile")
        private KakaoProfile profile;
    }

    @Getter
    @NoArgsConstructor
    static class KakaoProfile {

        private String nickname;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;
    }
}
