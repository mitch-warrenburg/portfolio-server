package com.mw.portfolio.security.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class CookieAuthentication extends FirebaseAuthentication {

  private final String cookie;

  @Override
  public Object getCredentials() {
    return this.cookie;
  }
}
