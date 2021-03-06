package com.mw.portfolio.user.service;

import static com.mw.portfolio.security.model.PrincipalRole.ROLE_ADMIN;
import static com.mw.portfolio.security.model.PrincipalRole.ROLE_ANONYMOUS;
import static java.util.Objects.nonNull;

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

  private final UserRepository repository;

  @PreAuthorize("#uid == authentication.name")
  public User getAuthorizedUser(@P("uid") String uid) {
    return getUser(uid);
  }

  public User getUser(String uid) {
    return repository.findByUid(uid)
        .orElseThrow(EntityNotFoundException::new);
  }

  public User getUserByUsername(String username) {
    return repository.findByUsername(username)
        .orElseThrow(EntityNotFoundException::new);
  }

  @PreAuthorize("#request.uid == authentication.name")
  public User updateUser(@P("request") UserUpdateRequest request) {

    val user = getUser(request.getUid());

    return repository.saveAndFlush(user.toBuilder()
        .role(user.getRole())
        .email(nonNull(request.getEmail()) ? request.getEmail() : user.getEmail())
        .company(nonNull(request.getCompany()) ? request.getCompany() : user.getCompany())
        .username(nonNull(request.getUsername()) ? request.getUsername() : user.getUsername())
        .build());
  }

  @PreAuthorize("#user.uid == authentication.name")
  public void persistAuthorizedUser(@P("user") User user) {
    saveUser(user);
  }

  public void saveUser(User user) {
    repository.save(user);
  }

  public GrantedAuthority getUserAuthorityRole(String uid) {
    val role = repository.getUserRole(uid)
        .orElse(ROLE_ANONYMOUS);

    return new SimpleGrantedAuthority(role.toString());
  }

  public void createUserIfNotPresent(FirebaseUserDetails userDetails) {
    if (!repository.existsByUid(userDetails.getUid())) {

      log.info("User not found.  Saving new authenticated user record. [uid]: {}", userDetails.getUid());

      repository.saveAndFlush(User.builder()
          .uid(userDetails.getUid())
          .phoneNumber(userDetails.getPhoneNumber())
          .build());
    }
  }

  public boolean doesUserExist(String uid) {
    return repository.existsByUid(uid);
  }

  public User getDefaultChatUser() {
    return repository.findFirstByRole(ROLE_ADMIN);
  }

  public void updateEmail(String uid, String email) {
    repository.updateEmail(uid, email);
  }
}
