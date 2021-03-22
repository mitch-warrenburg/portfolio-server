package com.mw.portfolio.email.exception;

public class EmailSendException extends RuntimeException implements EmailException {
  public EmailSendException(Throwable cause) {
    super(cause);
  }
}
