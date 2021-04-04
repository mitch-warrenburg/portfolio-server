package com.mw.portfolio.security.service;

import com.mw.portfolio.security.model.FirebaseUserDetails;
import org.springframework.security.authentication.*;

public abstract class AbstractAuthenticationManager implements ReactiveAuthenticationManager {

  protected void runAuthorizations(FirebaseUserDetails userDetails) {

    if (!userDetails.isAccountNonLocked()) {
      throw new LockedException("User account is locked");
    }
    if (!userDetails.isEnabled()) {
      throw new DisabledException("User account is disabled");
    }
    if (!userDetails.isAccountNonExpired()) {
      throw new AccountExpiredException("User account is expired");
    }
    if (!userDetails.isCredentialsNonExpired()) {
      throw new CredentialsExpiredException("User account credentials have expired");
    }
  }
}
