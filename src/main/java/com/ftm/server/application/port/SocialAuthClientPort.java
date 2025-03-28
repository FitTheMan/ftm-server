package com.ftm.server.application.port;

import com.ftm.server.application.dto.command.SocialAuthCommand;
import com.ftm.server.infrastructure.oauth.SocialAuthUser;

public interface SocialAuthClientPort<T extends SocialAuthCommand, R extends SocialAuthUser> {

    // OAuthClient 인증 (소셜 인증)
    R authenticate(T command);
}
