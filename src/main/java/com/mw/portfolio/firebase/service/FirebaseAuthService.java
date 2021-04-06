package com.mw.portfolio.firebase.service;

import static com.mw.portfolio.config.security.Constants.*;
import static com.mw.portfolio.util.CookieUtil.generateCookie;
import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofDays;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SessionCookieOptions;
import com.mw.portfolio.firebase.converter.UserRecordToUserDetailsConverter;
import com.mw.portfolio.security.model.FirebaseUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Log4j2
@Service
@Profile("!mockAuth")
@AllArgsConstructor
public class FirebaseAuthService implements AuthService {

  private final FirebaseAuth auth;
  private final FirebaseLogService logService;
  private final UserRecordToUserDetailsConverter converter;

  @Override
  public FirebaseUserDetails authenticate(String authToken) throws FirebaseAuthException {

    val decodedToken = auth.verifyIdToken(authToken, true);
    val authTime = SECONDS.toMillis((long) decodedToken.getClaims().get("auth_time"));
    val isExpired = currentTimeMillis() - authTime >= MINUTES.toMillis(5);

    if (isExpired) {
      throw new CredentialsExpiredException(EXPIRED_TOKEN_EXCEPTION_MSG);
    }

    val userRecord = auth.getUser(decodedToken.getUid());

    logService.logSuccess("Auth token validation", userRecord, decodedToken);

    return converter.convert(userRecord);
  }

  @Override
  public FirebaseUserDetails verifySession(String cookie) throws FirebaseAuthException {

    val decodedToken = auth.verifySessionCookie(cookie, true);
    val userRecord = auth.getUser(decodedToken.getUid());

    logService.logSuccess("Session cookie verification", userRecord, decodedToken);

    return converter.convert(userRecord);
  }

  @Override
  public Cookie createSessionCookie(String authToken) {

    try {
      val options = SessionCookieOptions.builder()
          .setExpiresIn(ofDays(MAX_SESSION_DURATION_DAYS).toMillis())
          .build();
      val cookieValue = auth.createSessionCookie(authToken, options);

      return generateCookie(SESSION_COOKIE_NAME, cookieValue, MAX_SESSION_DURATION_DAYS);

    } catch (FirebaseAuthException exception) {
      log.error(exception);
      throw new RuntimeException(exception);
    }
  }
}
