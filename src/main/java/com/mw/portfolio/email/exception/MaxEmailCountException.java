package com.mw.portfolio.email.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MaxEmailCountException extends EmailException {

  public MaxEmailCountException(String message) {
    super(message);
  }

  public MaxEmailCountException(Throwable cause) {
    super(cause);
  }
}
