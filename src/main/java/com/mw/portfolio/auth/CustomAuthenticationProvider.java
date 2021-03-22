package com.mw.portfolio.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    return new PreAuthenticatedAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), List.of(new SimpleGrantedAuthority("USER_ANONYMOUS")));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return PreAuthenticatedAuthenticationToken.class.equals(authentication);
  }
}