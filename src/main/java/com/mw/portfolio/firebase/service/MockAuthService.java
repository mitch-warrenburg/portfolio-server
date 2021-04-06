package com.mw.portfolio.firebase.service;

import static com.mw.portfolio.config.security.Constants.MAX_SESSION_DURATION_DAYS;
import static com.mw.portfolio.config.security.Constants.SESSION_COOKIE_NAME;
import static com.mw.portfolio.util.CookieUtil.generateCookie;
import static java.lang.Thread.sleep;
import static java.util.Objects.isNull;

import com.google.firebase.ErrorCode;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthException;
import com.mw.portfolio.security.model.FirebaseUserDetails;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Profile("mockAuth")
@AllArgsConstructor
public class MockAuthService implements AuthService {

  private final UserService userService;

  private final AtomicReference<String> fakeAuthToken = new AtomicReference<>(UUID.randomUUID().toString());

  private final FirebaseUserDetails userDetails = FirebaseUserDetails.builder()
      .enabled(true)
      .uid("mockUser")
      .phoneNumber("+19999999999")
      .build();


  @PostConstruct
  public void initMockUser() {
    userService.createUserIfNotPresent(userDetails);
  }

  @Override
  public FirebaseUserDetails authenticate(String authToken) throws FirebaseAuthException {

    try {
      sleep(300);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    if ("mock".equals(authToken)) {
      throwFakeException("Auth token is invalid.");
    }

    return userDetails;
  }

  @Override
  public FirebaseUserDetails verifySession(String cookie) throws FirebaseAuthException {

    try {
      sleep(300);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    if (isNull(cookie) || isNull(fakeAuthToken.get())) {
      throwFakeException("No cookie found for session.  Authentication required.");
    }

    if (cookie.contains("mock")) {
      throwFakeException("Auth token is invalid.");
    }

    return userDetails;
  }

  @Override
  @SneakyThrows
  public Cookie createSessionCookie(String authToken) {

    if (isNull(authToken)) {
      throwFakeException("Authentication token is missing.");
    }

    return generateCookie(SESSION_COOKIE_NAME, fakeAuthToken.getAndUpdate(v -> UUID.randomUUID().toString()), MAX_SESSION_DURATION_DAYS);
  }

  private void throwFakeException(String message) throws FirebaseAuthException {
    fakeAuthToken.set(null);
    throw new FirebaseAuthException(new FirebaseException(ErrorCode.UNAUTHENTICATED, message, new RuntimeException("")));
  }
}
