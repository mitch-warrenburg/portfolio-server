package com.mw.portfolio.scheduling.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledEventRequest {

  @Email
  private String email;

  @NotBlank
  private String uid;

  @NotBlank
  private String start;

  @NotBlank
  private String end;
}
