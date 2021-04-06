package com.mw.portfolio.config.security;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.Filter;

@Log4j2
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final Filter authProcessingFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http
        .httpBasic()
          .disable()
        .csrf()
          .disable()
        .formLogin()
          .disable()
        .logout()
          .disable()
        .addFilterBefore(authProcessingFilter, AnonymousAuthenticationFilter.class)
        .authorizeRequests()
          .anyRequest()
            .authenticated();
    //@formatter:on
  }
}
