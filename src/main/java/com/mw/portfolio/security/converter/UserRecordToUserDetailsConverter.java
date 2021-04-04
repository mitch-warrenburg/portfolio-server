package com.mw.portfolio.security.converter;

import com.google.firebase.auth.UserRecord;
import com.mw.portfolio.security.model.FirebaseUserDetails;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserRecordToUserDetailsConverter implements Converter<UserRecord, FirebaseUserDetails> {

  @Override
  public FirebaseUserDetails convert(UserRecord user) {
    return FirebaseUserDetails.builder()
        .uid(user.getUid())
        .phoneNumber(user.getPhoneNumber())
        .build();
  }
}
