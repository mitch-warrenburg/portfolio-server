package com.mw.portfolio.security.repository;

import com.mw.portfolio.security.model.PreAuthenticatedSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class FirebaseContextRepository extends WebSessionServerSecurityContextRepository {

  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    return super.save(exchange, context);
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {
    return Mono.just(new PreAuthenticatedSecurityContext());
  }
}
