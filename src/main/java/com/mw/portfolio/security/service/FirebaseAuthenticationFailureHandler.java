package com.mw.portfolio.security.service;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class FirebaseAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

  @Override
  public Mono<Void> onAuthenticationFailure(WebFilterExchange filter, AuthenticationException exception) {

    val requestPath = filter.getExchange().getRequest().getPath().toString();
    val exceptionType = exception.getClass().getSimpleName();

    log.error("Authentication failure.  [type]: {} [requestPath]: {} [message]: {}", exceptionType, requestPath, exception.getMessage());

    return Mono.error(exception);
  }
}
