package com.mw.portfolio.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.Filter;

@Log4j2
@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final Filter authProcessingFilter;
  private final AuthenticationProvider adminAuthProvider;

  @Override
  protected void configure(AuthenticationManagerBuilder authManager) {
    authManager.authenticationProvider(adminAuthProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
    http
        .csrf()
          .disable()
        .formLogin()
          .disable()
        .logout()
          .disable()
        .httpBasic(withDefaults())
        .authorizeRequests()
          .antMatchers("/actuator/health")
            .permitAll()
          .antMatchers("/api/v1/admin/**", "/actuator/**")
            .authenticated()
        .and()
        .addFilterBefore(authProcessingFilter, AnonymousAuthenticationFilter.class)
        .authorizeRequests()
          .anyRequest()
            .authenticated();
    //@formatter:on
  }
}
