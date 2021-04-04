package com.mw.portfolio.security.converter;

import static com.mw.portfolio.config.security.SecurityConfig.SESSION_COOKIE_NAME;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.mw.portfolio.security.model.FirebaseAuthentication;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@AllArgsConstructor
public class FirebaseAuthenticationConverter implements ServerAuthenticationConverter {

  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {
    val cookie = exchange.getRequest().getCookies().getFirst(SESSION_COOKIE_NAME);

    var auth = FirebaseAuthentication.builder()
        .cookie(nonNull(cookie) ? cookie.getValue() : null)
        .token(exchange.getRequest().getHeaders().getFirst(AUTHORIZATION))
        .build();

    return Mono.justOrEmpty(auth);
  }
}
