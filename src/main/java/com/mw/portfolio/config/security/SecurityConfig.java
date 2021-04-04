package com.mw.portfolio.config.security;

import static com.mw.portfolio.security.model.UserRole.ROLE_USER;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

import com.mw.portfolio.security.converter.FirebaseAuthenticationConverter;
import com.mw.portfolio.security.repository.FirebaseContextRepository;
import com.mw.portfolio.security.service.FirebaseAuthenticationFailureHandler;
import com.mw.portfolio.security.service.FirebaseAuthenticationManagerResolver;
import com.mw.portfolio.security.service.FirebaseAuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.AndServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.session.data.redis.ReactiveRedisSessionRepository;
import org.springframework.web.server.WebFilter;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Configuration
@AllArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

  public static final int MAX_SESSION_DURATION_DAYS = 7;
  public static final String ACTUATOR_PATH_MATCHER = "/actuator/**";
  public static final String SAME_SITE_COOKIE_ATTRIBUTE = "Strict";
  public static final String SESSION_COOKIE_NAME = "FB_SESSION";
  public static final String INVALID_COOKIE_EXCEPTION_MSG = "The session cookie is invalid.";
  public static final String EXPIRED_TOKEN_EXCEPTION_MSG = "The authorization token is expired.";
  public static final String INVALID_TOKEN_EXCEPTION_MSG = "The authorization token is invalid.";
  public static final String AUTH_NOT_FOUND_MSG = "The required authentication parameters were not provided.";

  private final FirebaseContextRepository contextRepository;
  private final FirebaseAuthenticationConverter authConverter;
  private final ReactiveRedisSessionRepository webSessionManager;
  private final FirebaseAuthenticationFailureHandler failureHandler;
  private final FirebaseAuthenticationSuccessHandler successHandler;
  private final FirebaseAuthenticationManagerResolver authManagerResolver;

  @PostConstruct
  public void test() {
    System.out.println(webSessionManager.getSessionRedisOperations());
  }


  @Bean
  public ServerWebExchangeMatcher authPathMatchers() {

    val matchers = Stream.of(PUT, POST, GET)
        .map(method -> pathMatchers(method, "/**"))
        .collect(Collectors.toList());
    val allPathsMatcher = new OrServerWebExchangeMatcher(matchers);
    val actuatorMatcher = new NegatedServerWebExchangeMatcher(pathMatchers(ACTUATOR_PATH_MATCHER));

    return new AndServerWebExchangeMatcher(allPathsMatcher, actuatorMatcher);
  }

  @Bean
  public WebFilter firebaseAuthWebFilter(ServerWebExchangeMatcher authPathMatchers) {
    val filter = new AuthenticationWebFilter(authManagerResolver);
    filter.setSecurityContextRepository(contextRepository);
    filter.setServerAuthenticationConverter(authConverter);
    filter.setAuthenticationFailureHandler(failureHandler);
    filter.setAuthenticationSuccessHandler(successHandler);
    filter.setRequiresAuthenticationMatcher(authPathMatchers);
    return filter;
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, WebFilter firebaseAuthWebFilter) {
    //@formatter:off
    return http
        .httpBasic()
          .disable()
        .formLogin()
          .disable()
        .logout()
          .disable()
        .csrf()
          .disable()
        .addFilterAt(firebaseAuthWebFilter, AUTHENTICATION)
        .securityContextRepository(contextRepository)
        .authorizeExchange()
          .pathMatchers(ACTUATOR_PATH_MATCHER)
            .permitAll()
          .anyExchange()
            .hasAuthority(ROLE_USER.toString())
        .and().build();
    //@formatter:on
  }
}