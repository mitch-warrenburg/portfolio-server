package com.mw.portfolio.security.controller;

import com.mw.portfolio.security.model.FirebaseAuthentication;
import com.mw.portfolio.security.service.UserAuthenticationService;
import com.mw.portfolio.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  private final UserAuthenticationService authService;

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  @PostAuthorize("principal == returnObject.phoneNumber")
  public Mono<User> authenticateUser(@AuthenticationPrincipal FirebaseAuthentication auth) {
    return authService.getAuthenticatedUser(auth);
  }
}
