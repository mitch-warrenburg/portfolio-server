package com.mw.portfolio.security.service;

import com.mw.portfolio.security.converter.UserToAdminUserDetailsConverter;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

  public final UserService userService;
  public final UserToAdminUserDetailsConverter converter;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      val user = userService.getUserByUsername(username);
      return converter.convert(user);
    } catch (Exception e) {
      throw new UsernameNotFoundException("Admin user not found.");
    }
  }
}
