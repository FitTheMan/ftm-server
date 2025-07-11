package com.ftm.server.infrastructure.session;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class ProtocolAwareCookieSerializer extends DefaultCookieSerializer {

    @Override
    public void writeCookieValue(CookieValue cookieValue) {
        HttpServletRequest request = cookieValue.getRequest();
        String proto = request.getHeader("X-Forwarded-Proto");

        boolean isHttps = request.isSecure() || "https".equalsIgnoreCase(proto);

        // HTTPS -> Secure O + SameSite=None
        // HTTP -> Secure X + SameSite null
        setUseSecureCookie(isHttps);
        setSameSite(isHttps ? "None" : null);

        super.writeCookieValue(cookieValue);
    }
}
