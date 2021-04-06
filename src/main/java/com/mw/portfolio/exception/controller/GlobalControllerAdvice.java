package com.mw.portfolio.exception.controller;

import static org.springframework.http.HttpStatus.*;

import com.mw.portfolio.email.exception.EmailException;
import com.mw.portfolio.exception.model.ErrorMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Log4j2
@Component
@RestControllerAdvice
public class GlobalControllerAdvice {

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler({EmailException.class})
  public ErrorMessage handleEmailExceptions(final Exception e, final HttpServletRequest request) {
    return buildAndLogErrorMessage(e, request, BAD_REQUEST);
  }

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ErrorMessage handleNotFoundExceptions(final EntityNotFoundException e, final HttpServletRequest request) {
    return buildAndLogErrorMessage(e, request, NOT_FOUND);
  }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(AuthenticationException.class)
  public ErrorMessage handleAuthenticationExceptions(final AuthenticationException e, final HttpServletRequest request) {
    return buildAndLogErrorMessage(e, request, UNAUTHORIZED);
  }

  @ResponseStatus(FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  public ErrorMessage handleAuthorizationExceptions(final AccessDeniedException e, final HttpServletRequest request) {
    return buildAndLogErrorMessage(e, request, FORBIDDEN);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler({Exception.class})
  public ErrorMessage handleAllOtherExceptions(final Exception e, final HttpServletRequest request) {
    return buildAndLogErrorMessage(e, request, BAD_REQUEST);
  }

  private ErrorMessage buildAndLogErrorMessage(final Exception e, final HttpServletRequest request, final HttpStatus status) {
    log.error("Exception caught by controller advice.  [type]: {} [requestPath]: {} [message]: {}", e.getClass().getSimpleName(), request.getRequestURI(), e.getMessage());
    return ErrorMessage.builder()
        .message(e.getMessage())
        .status(status.value())
        .timestamp(LocalDateTime.now())
        .path(request.getRequestURI())
        .build();
  }
}
