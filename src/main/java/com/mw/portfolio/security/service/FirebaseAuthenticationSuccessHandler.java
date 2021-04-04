package com.mw.portfolio.security.service;


import com.mw.portfolio.security.model.FirebaseAuthentication;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class FirebaseAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

  private final UserService userService;
  private final AuthenticationService authService;

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange filter, Authentication authentication) {

    val auth = (FirebaseAuthentication) authentication;

    if (auth.isNewSession()) {
      val token = authService.createSessionCookie(auth.getToken());
      filter.getExchange().getResponse().addCookie(token);
      userService.createUserIfNotPresent(auth.getDetails());
    }

    filter.getExchange().getAttributes().put("auth", authentication);

    return filter.getChain().filter(filter.getExchange());
  }
}
