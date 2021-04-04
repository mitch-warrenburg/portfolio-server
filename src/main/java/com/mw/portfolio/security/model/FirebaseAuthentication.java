package com.mw.portfolio.security.model;

import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FirebaseAuthentication implements Authentication, CredentialsContainer, Serializable {

  private String token;
  private String cookie;
  private boolean isNewSession;
  private boolean authenticated;

  @Singular
  private List<GrantedAuthority> authorities;

  @Builder.Default
  private FirebaseUserDetails details = FirebaseUserDetails.builder().build();

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return Optional.ofNullable(details)
        .map(FirebaseUserDetails::getPhoneNumber)
        .orElse(null);
  }

  @Override
  public String getName() {
    return Optional.ofNullable(details)
        .map(FirebaseUserDetails::getUsername)
        .orElse(null);
  }

  @Override
  public void eraseCredentials() {
    this.setDetails(null);
    this.getAuthorities().clear();
    this.setAuthenticated(false);
  }
}
