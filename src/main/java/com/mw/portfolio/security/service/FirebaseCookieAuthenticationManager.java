package com.mw.portfolio.security.service;

import static com.mw.portfolio.security.model.UserRole.ROLE_USER;

import com.mw.portfolio.security.model.FirebaseAuthentication;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import reactor.core.publisher.Mono;

@Log4j2
@AllArgsConstructor
public class FirebaseCookieAuthenticationManager extends AbstractAuthenticationManager {

  private final AuthenticationService authService;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {

    try {

      val firebaseAuth = (FirebaseAuthentication) authentication;

      return Mono.justOrEmpty(firebaseAuth)
          .map(FirebaseAuthentication::getCookie)
          .map(authService::verifySession)
          .doOnNext(this::runAuthorizations)
          .map(userDetails -> firebaseAuth.toBuilder()
              .authenticated(true)
              .details(userDetails)
              .authority(new SimpleGrantedAuthority(ROLE_USER.toString()))
              .build());

    } catch (Exception e) {
      throw new SessionAuthenticationException(e.getMessage());
    }
  }
}
