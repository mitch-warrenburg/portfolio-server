package com.mw.portfolio.security.service;

import static com.mw.portfolio.config.security.SecurityConfig.*;
import static com.mw.portfolio.security.model.UserRole.ROLE_USER;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.firebase.auth.*;
import com.mw.portfolio.security.converter.UserRecordToUserDetailsConverter;
import com.mw.portfolio.security.model.FirebaseUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Log4j2
@Service
@Profile("!mockAuth")
@AllArgsConstructor
public class FirebaseAuthenticationService implements AuthenticationService {

  private final FirebaseAuth auth;
  private final UserRecordToUserDetailsConverter converter;

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");

  static {
    dateTimeFormatter.withZone(ZoneId.systemDefault());
  }

  @Override
  public FirebaseUserDetails authenticate(String authToken) {

    try {

      val decodedToken = auth.verifyIdToken(authToken, true);
      val authTime = SECONDS.toMillis((long) decodedToken.getClaims().get("auth_time"));
      val isExpired = currentTimeMillis() - authTime >= MINUTES.toMillis(5);

      if (isExpired) {
        throw new CredentialsExpiredException(EXPIRED_TOKEN_EXCEPTION_MSG);
      }

      val userRecord = auth.getUser(decodedToken.getUid());

      logSuccess("Auth token validation", userRecord, decodedToken);

      return
          FirebaseUserDetails.builder()
              .uid(userRecord.getUid())
              .enabled(!userRecord.isDisabled())
              .phoneNumber(userRecord.getPhoneNumber())
              .authority(new SimpleGrantedAuthority(ROLE_USER.toString()))
              .build();

    } catch (FirebaseAuthException exception) {
      log.error(exception);
      throw new SessionAuthenticationException(INVALID_TOKEN_EXCEPTION_MSG);
    }
  }

  @Override
  public FirebaseUserDetails verifySession(String cookie) {

    try {
      val decodedToken = auth.verifySessionCookie(cookie, true);
      val userRecord = auth.getUser(decodedToken.getUid());

      logSuccess("Session cookie verification", userRecord, decodedToken);

      return converter.convert(userRecord);

    } catch (FirebaseAuthException exception) {
      log.error(exception);
      throw new SessionAuthenticationException(INVALID_COOKIE_EXCEPTION_MSG);
    }
  }

  @Override
  public ResponseCookie createSessionCookie(String authToken) {

    try {
      val maxAge = Duration.ofDays(MAX_SESSION_DURATION_DAYS);
      val options = SessionCookieOptions.builder()
          .setExpiresIn(maxAge.toMillis())
          .build();
      val sessionCookie = auth.createSessionCookie(authToken, options);

      return ResponseCookie.from(SESSION_COOKIE_NAME, sessionCookie)
          .secure(false)
          .httpOnly(true)
          .maxAge(maxAge)
          .sameSite(SAME_SITE_COOKIE_ATTRIBUTE)
          .build();

    } catch (FirebaseAuthException exception) {
      log.error(exception);
      throw new RuntimeException(exception);
    }
  }

  private void logSuccess(String successType, UserRecord userRecord, FirebaseToken decodedToken) {
    log.info("{} success. [uid]: {} [phoneNumber]: {} [authTime]: {} [expiryTime]: {}", successType, userRecord.getUid(), userRecord.getUid(), claimToDateString("auth_time", decodedToken), claimToDateString("exp", decodedToken));
  }

  private long getClaimEpoch(String property, FirebaseToken decodedToken) {
    return 1000 * (long) decodedToken.getClaims().get(property);
  }

  private String epochToDateString(long epoch) {
    return dateTimeFormatter.format(ofInstant(ofEpochMilli(epoch), ZoneId.systemDefault()));
  }

  private String claimToDateString(String property, FirebaseToken decodedToken) {
    return epochToDateString(getClaimEpoch(property, decodedToken));
  }
}
