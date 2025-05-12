package com.ftm.server.common.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtils {

    public static Cookie invalidateCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        return cookie;
    }
}
