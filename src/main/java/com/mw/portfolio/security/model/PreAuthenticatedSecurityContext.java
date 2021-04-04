package com.mw.portfolio.security.model;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Data
public class PreAuthenticatedSecurityContext implements SecurityContext {

  private Authentication authentication;

  public PreAuthenticatedSecurityContext() {
    this.authentication = new PreAuthenticatedAuthenticationToken(null, null);
    this.authentication.setAuthenticated(true);
  }

  @Override
  public Authentication getAuthentication() {
    return authentication;
  }

  @Override
  public void setAuthentication(Authentication authentication) {
    this.authentication = authentication;
  }
}
