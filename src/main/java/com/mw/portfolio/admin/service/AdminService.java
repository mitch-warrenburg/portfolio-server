package com.mw.portfolio.admin.service;

import static com.mw.portfolio.security.model.PrincipalRole.ROLE_ADMIN;
import static com.mw.portfolio.security.model.PrincipalRole.ROLE_SYSTEM;

import com.mw.portfolio.config.security.SecurityProperties;
import com.mw.portfolio.config.security.SecurityProperties.PrivilegedUser;
import com.mw.portfolio.security.model.PrincipalRole;
import com.mw.portfolio.user.entity.User;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

  private final UserService userService;
  private final SecurityProperties properties;
  private final PasswordEncoder passwordEncoder;

  public User getAuthenticatedAdminUser() {
    val adminUser = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    return userService.getUser(adminUser.getName());
  }

  public List<String> getRolesForUser(String uid) {
    return List.of(userService.getUserAuthorityRole(uid).getAuthority());
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createAdminUserIfNotPresent() {

    val admin = properties.getAdmin();
    val system = properties.getSystem();

    if (!userService.doesUserExist(admin.getUid())) {
      createPrivilegedUser(admin, ROLE_ADMIN);
    }

    if (!userService.doesUserExist(system.getUid())) {
      createPrivilegedUser(system, ROLE_SYSTEM);
    }
  }

  private void createPrivilegedUser(PrivilegedUser user, PrincipalRole role) {
    userService.saveUser(User.builder()
        .role(role)
        .uid(user.getUid())
        .username(user.getUid())
        .password(passwordEncoder.encode(user.getPassword()))
        .build());
  }
}
