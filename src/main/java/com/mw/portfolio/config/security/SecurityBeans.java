package com.mw.portfolio.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class SecurityBeans {

  @Bean
  public RequestMatcher allAuthenticatedPathsMatcher() {
    return new AndRequestMatcher(
        new NegatedRequestMatcher(new AntPathRequestMatcher("/actuator/**")),
        new NegatedRequestMatcher(new AntPathRequestMatcher("/api/v1/admin/**"))
    );
  }

  @Bean
  public RequestMatcher tokenAuthMatcher() {
    return new AntPathRequestMatcher("/api/v1/auth");
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationProvider tokenAuthProvider,
                                                     AuthenticationProvider cookieAuthProvider) {
    return new ProviderManager(tokenAuthProvider, cookieAuthProvider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider adminAuthProvider(UserDetailsService adminUserDetailsService) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(adminUserDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }
}
