package com.mw.portfolio.config.security;

import static org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse;

import com.mw.portfolio.auth.CustomAuthenticationProcessingFilter;
import com.mw.portfolio.auth.CustomAuthenticationProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomAuthenticationProvider provider;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.csrfTokenRepository(withHttpOnlyFalse()))
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .httpBasic().disable()
        .formLogin().disable()
        .logout().disable()
        .authenticationProvider(provider)
        .addFilterBefore(getFilter(), AnonymousAuthenticationFilter.class)
        .authorizeRequests();
//        .requestMatchers(getRequestMatchers())
//        .authenticated();
  }

  private RequestMatcher getRequestMatchers() {
    return new OrRequestMatcher(new AntPathRequestMatcher("/**"));
  }

  private Filter getFilter() throws Exception {
    return new CustomAuthenticationProcessingFilter(getRequestMatchers(), authenticationManager());
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(provider);
  }
}