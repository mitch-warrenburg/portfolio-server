package com.mw.portfolio.security.service;

import static com.mw.portfolio.config.security.SecurityConfig.*;
import static com.mw.portfolio.security.model.UserRole.ROLE_USER;

import com.mw.portfolio.security.model.FirebaseUserDetails;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.UUID;

@Service
@Profile("mockAuth")
@AllArgsConstructor
public class MockAuthenticationService implements AuthenticationService {

  private static final String MOCK_PHONE_NUMBER = "+19997326994";
  private static final String MOCK_ID = UUID.randomUUID().toString();

  private final UserService userService;

  @PostConstruct
  public void initMockUser() {
    userService.createUserIfNotPresent(FirebaseUserDetails.builder()
        .uid(MOCK_ID)
        .phoneNumber(MOCK_PHONE_NUMBER)
        .build());
  }

  @Override
  public FirebaseUserDetails authenticate(String authToken) {
    return FirebaseUserDetails.builder()
        .uid(MOCK_ID)
        .enabled(true)
        .phoneNumber(MOCK_PHONE_NUMBER)
        .authority(new SimpleGrantedAuthority(ROLE_USER.toString()))
        .build();
  }

  @Override
  public FirebaseUserDetails verifySession(String cookie) {
    return FirebaseUserDetails.builder()
        .uid(MOCK_ID)
        .phoneNumber(MOCK_PHONE_NUMBER)
        .authority(new SimpleGrantedAuthority(ROLE_USER.toString()))
        .build();
  }

  @Override
  public ResponseCookie createSessionCookie(String authToken) {
    return ResponseCookie.from(SESSION_COOKIE_NAME, authToken)
        .secure(false)
        .httpOnly(true)
        .maxAge(Duration.ofDays(MAX_SESSION_DURATION_DAYS))
        .sameSite(SAME_SITE_COOKIE_ATTRIBUTE)
        .build();
  }
}
