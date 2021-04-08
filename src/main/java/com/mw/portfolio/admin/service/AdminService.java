package com.mw.portfolio.admin.service;

import static com.mw.portfolio.security.model.PrincipalRole.ROLE_ADMIN;
import static com.mw.portfolio.security.model.PrincipalRole.ROLE_SYSTEM;

import com.google.firebase.auth.FirebaseAuthException;
import com.mw.portfolio.admin.converter.UserToAdminUserResponseConverter;
import com.mw.portfolio.admin.model.AdminUserResponse;
import com.mw.portfolio.chat.modal.ChatUserResponse;
import com.mw.portfolio.chat.service.ChatService;
import com.mw.portfolio.config.security.SecurityProperties;
import com.mw.portfolio.config.security.SecurityProperties.PrivilegedUser;
import com.mw.portfolio.firebase.service.FirebaseAuthService;
import com.mw.portfolio.security.model.PrincipalRole;
import com.mw.portfolio.user.entity.User;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {

  private final UserService userService;
  private final ChatService chatService;
  private final SecurityProperties properties;
  private final PasswordEncoder passwordEncoder;
  private final FirebaseAuthService authService;
  private final UserToAdminUserResponseConverter converter;

  public AdminUserResponse getUserAsAdmin(String uid) {
    val user = userService.getUser(uid);
    return converter.convert(user);
  }

  public ChatUserResponse getChatUserAsAdmin() throws FirebaseAuthException {
    val user = chatService.getDefaultChatUser();
    return user.toBuilder()
        .sessionId(properties.getChatSessionId())
        .token(authService.loginAdmin(user.getUid()))
        .build();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createAdminUserIfNotPresent() {
    createOrUpdatePrivilegedUser(properties.getAdmin(), ROLE_ADMIN);
    createOrUpdatePrivilegedUser(properties.getSystem(), ROLE_SYSTEM);
  }

  private void createOrUpdatePrivilegedUser(PrivilegedUser user, PrincipalRole role) {
    if (!userService.doesUserExist(user.getUid())) {
      createPrivilegedUser(user, role);
    } else {
      updatePrivilegedUser(user, role);
    }
  }

  private void createPrivilegedUser(PrivilegedUser user, PrincipalRole role) {
    userService.saveUser(User.builder()
        .role(role)
        .uid(user.getUid())
        .username(user.getUsername())
        .password(passwordEncoder.encode(user.getPassword()))
        .build());
  }

  private void updatePrivilegedUser(PrivilegedUser user, PrincipalRole role) {
    val updatedUser = userService.getUser(user.getUid()).toBuilder()
        .role(role)
        .uid(user.getUid())
        .username(user.getUsername())
        .password(passwordEncoder.encode(user.getPassword()))
        .build();

    userService.saveUser(updatedUser);
  }
}
