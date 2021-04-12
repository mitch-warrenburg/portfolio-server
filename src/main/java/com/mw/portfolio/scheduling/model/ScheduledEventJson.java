package com.mw.portfolio.scheduling.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledEventJson {

  private Long id;
  private String borderColor;
  private String backgroundColor;

  @NotBlank
  private String start;

  @NotBlank
  private String end;

  @NotNull
  private ExtendedProps extendedProps;

  @Data
  @With
  @Builder
  @Validated
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ExtendedProps {

    private boolean currentUser;
  }
}
