package com.mw.portfolio.user.service;

import com.mw.portfolio.security.model.FirebaseUserDetails;
import com.mw.portfolio.user.entity.User;
import com.mw.portfolio.user.model.UserUpdateRequest;
import com.mw.portfolio.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User getUser(String uid) {
    return userRepository.findByUid(uid)
        .orElseThrow(EntityNotFoundException::new);
  }

  @PreAuthorize("#request.uid == authentication.name")
  public Mono<User> updateUser(@P("request") UserUpdateRequest request) {

    val user = getUser(request.getUid());

    return Mono.just(userRepository.save(user.toBuilder()
        .email(request.getEmail())
        .company(request.getCompany())
        .username(request.getUsername())
        .build()));
  }

  public void createUserIfNotPresent(FirebaseUserDetails userDetails) {
    if (!userRepository.existsByUid(userDetails.getUid())) {
      userRepository.saveAndFlush(User.builder()
          .uid(userDetails.getUid())
          .phoneNumber(userDetails.getPhoneNumber())
          .build());
    }
  }
}
