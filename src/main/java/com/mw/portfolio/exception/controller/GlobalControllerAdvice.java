package com.mw.portfolio.exception.controller;

import static org.springframework.http.HttpStatus.*;

import com.mw.portfolio.exception.model.ErrorMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Log4j2
@Component
@RestControllerAdvice
public class GlobalControllerAdvice {

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ErrorMessage handleNotFoundExceptions(final Exception e, final ServerWebExchange exchange) {
    return buildAndLogErrorMessage(e, exchange, NOT_FOUND);
  }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(AuthenticationException.class)
  public ErrorMessage handleAuthenticationExceptions(final AuthenticationException e, final ServerWebExchange exchange) {
    return buildAndLogErrorMessage(e, exchange, UNAUTHORIZED);
  }

  @ResponseStatus(FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  public ErrorMessage handleAuthorizationExceptions(final AccessDeniedException e, final ServerWebExchange exchange) {
    return buildAndLogErrorMessage(e, exchange, FORBIDDEN);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler({Exception.class})
  public ErrorMessage handleAllOtherExceptions(final Exception e, final ServerWebExchange exchange) {
    return buildAndLogErrorMessage(e, exchange, BAD_REQUEST);
  }

  private ErrorMessage buildAndLogErrorMessage(final Exception e, final ServerWebExchange exchange, final HttpStatus status) {
    log.error("Exception caught by controller advice.  [type]: {} [requestPath]: {} [message]: {}", e.getClass().getSimpleName(), exchange.getRequest().getPath(), e.getMessage());
    return ErrorMessage.builder()
        .message(e.getMessage())
        .status(status.value())
        .timestamp(LocalDateTime.now())
        .path(exchange.getRequest().getPath().toString())
        .build();
  }
}
