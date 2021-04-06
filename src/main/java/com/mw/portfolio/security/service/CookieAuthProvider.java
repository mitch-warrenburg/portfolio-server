package com.mw.portfolio.security.service;

import static com.mw.portfolio.config.security.Constants.INVALID_COOKIE_EXCEPTION_MSG;
import static com.mw.portfolio.config.security.Constants.MISSING_COOKIE_EXCEPTION_MSG;
import static java.util.Objects.isNull;

import com.mw.portfolio.firebase.service.AuthService;
import com.mw.portfolio.security.model.CookieAuthentication;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class CookieAuthProvider extends AbstractAuthProvider {

  private final AuthService authService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    log.info("Attempting to authenticate cookie.");

    if (isNull(authentication.getCredentials())) {
      throw new InvalidCookieException(MISSING_COOKIE_EXCEPTION_MSG);
    }

    try {

      val auth = (CookieAuthentication) authentication;
      val userDetails = authService.verifySession(auth.getCredentials().toString());

      runAuthorizations(userDetails);

      return auth.toBuilder()
          .authenticated(true)
          .details(userDetails)
          .authorities(userDetails.getAuthorities())
          .build();

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new InvalidCookieException(INVALID_COOKIE_EXCEPTION_MSG);
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(CookieAuthentication.class);
  }
}
