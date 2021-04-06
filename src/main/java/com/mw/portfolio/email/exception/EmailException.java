package com.mw.portfolio.email.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class EmailException extends RuntimeException {

  public EmailException(String message) {
    super(message);
  }

  public EmailException(String message, Throwable cause) {
    super(message, cause);
  }

  public EmailException(Throwable cause) {
    super(cause);
  }
}
