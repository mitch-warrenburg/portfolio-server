package com.mw.portfolio.security.service;

import static com.mw.portfolio.config.security.Constants.USER_NOT_FOUND_EXCEPTION_MSG;
import static java.util.Objects.isNull;

import com.mw.portfolio.security.model.FirebaseUserDetails;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class AbstractAuthProvider implements AuthenticationProvider {

  protected void runAuthorizations(@Nullable FirebaseUserDetails userDetails) {
    if (isNull(userDetails)) {
      throw new UsernameNotFoundException(USER_NOT_FOUND_EXCEPTION_MSG);
    }
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
