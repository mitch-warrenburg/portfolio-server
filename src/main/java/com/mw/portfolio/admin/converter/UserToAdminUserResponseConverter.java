package com.mw.portfolio.admin.converter;

import static java.util.Objects.nonNull;

import com.mw.portfolio.admin.model.AdminUserResponse;
import com.mw.portfolio.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserToAdminUserResponseConverter implements Converter<User, AdminUserResponse> {
  @Override
  public AdminUserResponse convert(User user) {
    return AdminUserResponse.builder()
        .uid(user.getUid())
        .role(user.getRole().toString())
        .username(nonNull(user.getUsername()) ? user.getUsername() : "Anonymous User")
        .build();
  }
}
