package kr.minimalest.api.web;

import jakarta.servlet.http.Cookie;

public class SafeCookie extends Cookie {

    private SafeCookie(String name, String value) {
        super(name, value);
        super.setSecure(true);
        super.setHttpOnly(true);
        super.setPath("/");
    }

    public static SafeCookie of(String name, String value, int maxAge) {
        SafeCookie safeCookie = new SafeCookie(name, value);
        safeCookie.setMaxAge(maxAge);
        return safeCookie;
    }
}
