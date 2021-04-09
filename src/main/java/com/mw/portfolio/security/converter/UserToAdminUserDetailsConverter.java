package com.mw.portfolio.security.converter;

import com.mw.portfolio.security.model.AdminUserDetails;
import com.mw.portfolio.user.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class UserToAdminUserDetailsConverter implements Converter<User, AdminUserDetails> {
  @Override
  public AdminUserDetails convert(User user) {
    return AdminUserDetails.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .authority(new SimpleGrantedAuthority(user.getRole().toString()))
        .build();
  }
}
