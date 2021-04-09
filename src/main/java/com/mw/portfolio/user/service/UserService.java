package com.mw.portfolio.user.service;

import static com.mw.portfolio.security.model.PrincipalRole.ROLE_ADMIN;
import static com.mw.portfolio.security.model.PrincipalRole.ROLE_ANONYMOUS;

import com.mw.portfolio.security.model.FirebaseUserDetails;
import com.mw.portfolio.user.entity.User;
import com.mw.portfolio.user.model.UserUpdateRequest;
import com.mw.portfolio.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @PreAuthorize("#uid == authentication.name")
  public User getAuthorizedUser(@P("uid") String uid) {
    return getUser(uid);
  }

  public User getUser(String uid) {
    return userRepository.findByUid(uid)
        .orElseThrow(EntityNotFoundException::new);
  }

  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(EntityNotFoundException::new);
  }

  @PreAuthorize("#request.uid == authentication.name")
  public User updateUser(@P("request") UserUpdateRequest request) {

    val user = getUser(request.getUid());

    return userRepository.saveAndFlush(user.toBuilder()
        .role(user.getRole())
        .email(request.getEmail())
        .company(request.getCompany())
        .username(request.getUsername())
        .build());
  }

  @PreAuthorize("#user.uid == authentication.name")
  public void persistAuthorizedUser(@P("user") User user) {
    saveUser(user);
  }

  public void saveUser(User user) {
    userRepository.save(user);
  }

  public GrantedAuthority getUserAuthorityRole(String uid) {
    val role = userRepository.getUserRole(uid)
        .orElse(ROLE_ANONYMOUS);

    return new SimpleGrantedAuthority(role.toString());
  }

  public void createUserIfNotPresent(FirebaseUserDetails userDetails) {
    if (!userRepository.existsByUid(userDetails.getUid())) {

      log.info("User not found.  Saving new authenticated user record. [uid]: {}", userDetails.getUid());

      userRepository.saveAndFlush(User.builder()
          .uid(userDetails.getUid())
          .phoneNumber(userDetails.getPhoneNumber())
          .build());
    }
  }

  public boolean doesUserExist(String uid) {
    return userRepository.existsByUid(uid);
  }

  public User getDefaultChatUser() {
    return userRepository.findFirstByRole(ROLE_ADMIN);
  }
}
