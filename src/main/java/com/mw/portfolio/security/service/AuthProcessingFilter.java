package com.mw.portfolio.security.service;

import static com.mw.portfolio.config.security.Constants.MISSING_COOKIE_EXCEPTION_MSG;
import static com.mw.portfolio.config.security.Constants.SESSION_COOKIE_NAME;
import static com.mw.portfolio.util.CookieUtil.extractCookie;

import com.mw.portfolio.security.model.CookieAuthentication;
import com.mw.portfolio.security.model.TokenAuthentication;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
public class AuthProcessingFilter extends AbstractAuthenticationProcessingFilter {

  private final RequestMatcher tokenAuthMatcher;

  public AuthProcessingFilter(RequestMatcher tokenAuthMatcher,
                              RequestMatcher allAuthenticatedPathsMatcher,
                              AuthenticationManager authenticationManager,
                              AuthenticationSuccessHandler authSuccessHandler,
                              AuthenticationFailureHandler authFailureHandler) {
    super(allAuthenticatedPathsMatcher, authenticationManager);
    super.setAuthenticationSuccessHandler(authSuccessHandler);
    super.setAuthenticationFailureHandler(authFailureHandler);
    this.tokenAuthMatcher = tokenAuthMatcher;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    log.info("*** Using AuthenticationProcessingFilter for request to {} ***", request.getRequestURI());

    val authentication = tokenAuthMatcher.matches(request)
        ? new TokenAuthentication(request.getHeader("Authorization"))
        : extractCookie(request, SESSION_COOKIE_NAME)
        .map(Cookie::getValue)
        .map(CookieAuthentication::new)
        .orElseThrow(() -> new InvalidCookieException(MISSING_COOKIE_EXCEPTION_MSG));

    return getAuthenticationManager().authenticate(authentication);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    log.info("Authentication processing filter success for {}", request.getRequestURI());
    super.successfulAuthentication(request, response, chain, authResult);
    chain.doFilter(request, response);
  }
}