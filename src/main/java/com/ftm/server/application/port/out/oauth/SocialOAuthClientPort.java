package com.ftm.server.application.port.out.oauth;

import com.ftm.server.application.command.auth.SocialLoginCommand;
import com.ftm.server.infrastructure.oauth.SocialAuthUser;

public interface SocialOAuthClientPort<T extends SocialLoginCommand, R extends SocialAuthUser> {

    // OAuthClient 인증 (소셜 인증)
    R authenticate(T command);
}
