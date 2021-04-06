package com.mw.portfolio.security.service;

import com.mw.portfolio.firebase.service.AuthService;
import com.mw.portfolio.security.model.FirebaseAuthentication;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Component
@AllArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

  private final AuthService authService;
  private final UserService userService;
  private final RequestMatcher tokenAuthMatcher;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    log.info("Invoking authentication success handler. [path]: {} [phoneNumber]: {} [username]: {}", request.getRequestURI(), authentication.getPrincipal(), authentication.getName());

    if (tokenAuthMatcher.matches(request)) {

      val auth = (FirebaseAuthentication) authentication;

      userService.createUserIfNotPresent(auth.getDetails());
      response.addCookie(authService.createSessionCookie(auth.getCredentials().toString()));

      auth.eraseCredentials();

      log.info("Token authentication successful.  Adding session cookie to response.");
    }
  }
}
