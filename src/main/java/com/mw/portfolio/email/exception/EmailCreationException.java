package com.mw.portfolio.email.exception;

public class EmailCreationException extends RuntimeException implements EmailException {
  public EmailCreationException(Throwable cause) {
    super(cause);
  }
}
