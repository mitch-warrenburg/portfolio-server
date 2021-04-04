package com.mw.portfolio.security.service;

import com.mw.portfolio.security.model.FirebaseAuthentication;
import com.mw.portfolio.user.entity.User;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserAuthenticationService {

  private final UserService userService;

  public Mono<User> getAuthenticatedUser(FirebaseAuthentication auth) {
    return Mono.just(userService.getUser(auth.getDetails().getUid()));
  }
}
