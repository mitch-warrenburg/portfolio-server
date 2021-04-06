package com.mw.portfolio.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class SecurityBeans {

  @Bean
  public RequestMatcher allAuthenticatedPathsMatcher() {
    return new AndRequestMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/actuator/**")));
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
}
