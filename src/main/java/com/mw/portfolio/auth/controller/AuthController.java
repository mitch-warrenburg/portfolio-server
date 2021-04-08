package com.mw.portfolio.auth.controller;

import com.mw.portfolio.auth.service.UserAuthService;
import com.mw.portfolio.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final UserAuthService authService;

  @PostMapping
  public User getAuthenticatedUser() {
    return authService.getAuthenticatedUser();
  }
}
