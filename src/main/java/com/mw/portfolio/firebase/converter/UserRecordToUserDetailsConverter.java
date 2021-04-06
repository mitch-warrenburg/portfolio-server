package com.mw.portfolio.firebase.converter;

import com.google.firebase.auth.UserRecord;
import com.mw.portfolio.security.model.FirebaseUserDetails;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserRecordToUserDetailsConverter implements Converter<UserRecord, FirebaseUserDetails> {

  private final UserService userService;

  @Override
  public FirebaseUserDetails convert(UserRecord user) {
    return FirebaseUserDetails.builder()
        .uid(user.getUid())
        .enabled(!user.isDisabled())
        .phoneNumber(user.getPhoneNumber())
        .authorities(List.of(userService.getUserAuthorityRole(user.getUid())))
        .build();
  }
}
