package com.mw.portfolio.security.service;

import static com.mw.portfolio.config.security.Constants.SESSION_COOKIE_NAME;
import static com.mw.portfolio.util.CookieUtil.invalidateCookieIfPresent;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mw.portfolio.exception.model.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Log4j2
@Component
@AllArgsConstructor
public class AuthFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper mapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

    log.info("Invoking authentication failure handler. [path]: {} [exception]: {}", request.getRequestURI(), exception.getClass().getSimpleName());

    invalidateCookieIfPresent(request, response, SESSION_COOKIE_NAME);

    val errorMessage = ErrorMessage.builder()
        .message(exception.getMessage())
        .status(401)
        .timestamp(LocalDateTime.now())
        .path(request.getRequestURI())
        .build();

    response.setStatus(401);
    response.setContentType(APPLICATION_JSON.getMimeType());
    response.getWriter().print(mapper.writeValueAsString(errorMessage));
  }
}
