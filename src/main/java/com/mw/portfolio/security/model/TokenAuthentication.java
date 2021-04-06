package com.mw.portfolio.security.model;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class TokenAuthentication extends FirebaseAuthentication {

  private final String token;

  @Override
  public Object getCredentials() {
    return this.token;
  }
}
