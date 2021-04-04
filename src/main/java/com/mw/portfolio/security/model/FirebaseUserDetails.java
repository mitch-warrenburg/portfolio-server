package com.mw.portfolio.security.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.List;

@Data
@Log4j2
@EqualsAndHashCode
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class FirebaseUserDetails implements UserDetails, Serializable {

  private String uid;
  private String phoneNumber;

  @Builder.Default
  private boolean enabled = true;

  @Singular
  private List<GrantedAuthority> authorities;

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    // from a security perspective, this is the username
    return uid;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
