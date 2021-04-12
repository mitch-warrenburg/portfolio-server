package com.mw.portfolio.config.security;

import static java.util.stream.Collectors.toList;

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

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class SecurityBeans {

  @Bean
  public RequestMatcher firebaseAuthPathsMatcher() {
    List<RequestMatcher> negatedPaths = Stream.of("/actuator/**", "/api/v1/admin/**", "/api/v1/chat/default-user")
        .map(path -> new NegatedRequestMatcher(new AntPathRequestMatcher(path)))
        .collect(toList());
    negatedPaths.add(new NegatedRequestMatcher(new AntPathRequestMatcher("/api/v1/scheduling/events", "GET")));
    return new AndRequestMatcher(negatedPaths);
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
