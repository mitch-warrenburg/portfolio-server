package com.mw.portfolio.util;

import static java.time.Duration.ofDays;
import static java.util.Optional.ofNullable;

import lombok.extern.log4j.Log4j2;
import lombok.val;

import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Stream;

@Log4j2
public final class CookieUtil {

  public static Cookie generateCookie(String name, @Nullable String value, int maxAgeDays) {
    val cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) ofDays(maxAgeDays).toSeconds());
    return cookie;
  }

  public static Cookie generateInvalidatedCookie(Cookie cookie) {
    val invalidatedCookie = new Cookie(cookie.getName(), "");
    invalidatedCookie.setMaxAge(0);
    invalidatedCookie.setHttpOnly(true);
    invalidatedCookie.setPath("/");
    return invalidatedCookie;
  }

  public static Optional<Cookie> extractCookie(HttpServletRequest request, String cookieName) {
    return ofNullable(request.getCookies())
        .map(Stream::of)
        .flatMap(cookies -> cookies.filter(cookie -> cookie.getName().equals(cookieName))
            .findAny());
  }

  public static void invalidateCookieIfPresent(HttpServletRequest request, HttpServletResponse response, String cookieName) {
    extractCookie(request, cookieName).ifPresent(cookie -> {
      log.info("Invalidating session cookie.");
      response.addCookie(generateInvalidatedCookie(cookie));
    });
  }
}
