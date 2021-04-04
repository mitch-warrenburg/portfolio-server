package com.mw.portfolio.exception.model;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

  private int status;
  private String path;
  private String message;

  @JsonFormat(shape = STRING, pattern = "MMM dd, yyyy hh:mm a", timezone = "America/Los_Angeles")
  private LocalDateTime timestamp;
}
