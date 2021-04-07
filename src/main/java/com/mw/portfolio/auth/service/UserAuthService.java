package com.mw.portfolio.auth.service;

import com.mw.portfolio.security.model.FirebaseAuthentication;
import com.mw.portfolio.user.entity.User;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAuthService {

  private final UserService userService;

  public User getAuthenticatedUser() {
    val authenticatedUser =  (FirebaseAuthentication) SecurityContextHolder.getContext().getAuthentication();
    return userService.getAuthorizedUser(authenticatedUser.getDetails().getUid());
  }
}
