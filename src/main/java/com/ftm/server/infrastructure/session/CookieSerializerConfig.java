package com.ftm.server.infrastructure.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;

@Configuration
public class CookieSerializerConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        return new ProtocolAwareCookieSerializer();
    }
}
