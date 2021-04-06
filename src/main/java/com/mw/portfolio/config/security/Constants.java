package com.mw.portfolio.config.security;

public class Constants {
  public static final int MAX_SESSION_DURATION_DAYS = 7;
  public static final String ACTUATOR_PATH_MATCHER = "/actuator/**";
  public static final String SAME_SITE_COOKIE_ATTRIBUTE = "Strict";
  public static final String SESSION_COOKIE_NAME = "FB_SESSION";
  public static final String INVALID_COOKIE_EXCEPTION_MSG = "The session cookie is invalid.";
  public static final String USER_NOT_FOUND_EXCEPTION_MSG = "The UserDetails were not found.";
  public static final String MISSING_COOKIE_EXCEPTION_MSG = "The session cookie is not present in the Authentication.";
  public static final String EXPIRED_TOKEN_EXCEPTION_MSG = "The authorization token is expired.";
  public static final String INVALID_TOKEN_EXCEPTION_MSG = "The authorization token is invalid.";
  public static final String AUTH_NOT_FOUND_MSG = "The required authentication parameters were not provided.";
}
