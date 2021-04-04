package com.mw.portfolio.security.service;

import static com.mw.portfolio.config.security.SecurityConfig.AUTH_NOT_FOUND_MSG;
import static com.mw.portfolio.config.security.SecurityConfig.SESSION_COOKIE_NAME;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class FirebaseAuthenticationManagerResolver implements ReactiveAuthenticationManagerResolver<ServerWebExchange> {

  private final AuthenticationService authService;

  @Override
  public Mono<ReactiveAuthenticationManager> resolve(ServerWebExchange exchange) {

    val cookie = exchange.getRequest().getCookies().getFirst(SESSION_COOKIE_NAME);

    return nonNull(cookie)
        ? Mono.just(new FirebaseCookieAuthenticationManager(authService))
        : Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(AUTHORIZATION))
        .switchIfEmpty(Mono.error(new AuthenticationCredentialsNotFoundException(AUTH_NOT_FOUND_MSG)))
        .thenReturn(new FirebaseTokenAuthenticationManager(authService));
  }
}
