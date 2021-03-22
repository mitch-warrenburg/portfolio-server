package com.mw.portfolio.auth;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  public CustomAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher,
                                              AuthenticationManager authenticationManager) {
    super(requiresAuthenticationRequestMatcher);
    setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    return getAuthenticationManager().authenticate(new PreAuthenticatedAuthenticationToken(request.getSession().getId(), null));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    getContext().setAuthentication(authResult);
    chain.doFilter(request, response);
  }
}