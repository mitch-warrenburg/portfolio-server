package com.mw.portfolio.config.security;

public class Constants {
  public static final int MAX_SESSION_DURATION_DAYS = 7;
  public static final String SESSION_COOKIE_NAME = "FB_SESSION";
  public static final String INVALID_COOKIE_EXCEPTION_MSG = "The session cookie is invalid.";
  public static final String USER_NOT_FOUND_EXCEPTION_MSG = "The UserDetails were not found.";
  public static final String EXPIRED_TOKEN_EXCEPTION_MSG = "The authorization token is expired.";
  public static final String INVALID_TOKEN_EXCEPTION_MSG = "The authorization token is invalid.";
  public static final String MISSING_COOKIE_EXCEPTION_MSG = "The session cookie is not present in the Authentication.";
  public static final String USER_RESOURCE_MISMATCH_EXCEPTION_MSG = "The requested resource does not belong to the current user.";

}
