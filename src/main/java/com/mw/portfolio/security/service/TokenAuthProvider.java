package com.mw.portfolio.security.service;

import static com.mw.portfolio.config.security.Constants.INVALID_TOKEN_EXCEPTION_MSG;
import static java.util.Objects.isNull;

import com.mw.portfolio.firebase.service.AuthService;
import com.mw.portfolio.security.model.TokenAuthentication;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class TokenAuthProvider extends AbstractAuthProvider {

  private final AuthService authService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    log.info("Attempting to authenticate token.");

    if (isNull(authentication.getCredentials())) {
      throw new BadCredentialsException(INVALID_TOKEN_EXCEPTION_MSG);
    }

    try {

      val auth = (TokenAuthentication) authentication;
      val userDetails = authService.authenticate(auth.getCredentials().toString());

      runAuthorizations(userDetails);

      return auth.toBuilder()
          .authenticated(true)
          .details(userDetails)
          .authorities(userDetails.getAuthorities())
          .build();

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new BadCredentialsException(INVALID_TOKEN_EXCEPTION_MSG);
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(TokenAuthentication.class);
  }
}
