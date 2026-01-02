package com.ftm.server.infrastructure.oauth.kakao;

import com.ftm.server.domain.enums.RedirectEnv;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kakao")
@Getter
@Setter
public class KakaoProperties {

    private String clientId;
    private String clientSecret;
    private String tokenUri;
    private String userInfoUri;
    private Map<String, String> redirectUris;

    public String getRedirectUriByEnv(RedirectEnv env) {
        String redirectUri = redirectUris.get(env.key());
        if (redirectUri != null && !redirectUri.isBlank()) {
            return redirectUri;
        }

        return redirectUris.get("local");
    }
}
