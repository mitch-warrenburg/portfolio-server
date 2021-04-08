package com.mw.portfolio.config.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
@Component
@ConfigurationProperties("security")
public class SecurityProperties {

  @NotBlank
  private String chatSessionId;

  @Valid
  @NotNull
  private PrivilegedUser admin;

  @Valid
  @NotNull
  private PrivilegedUser system;

  @Data
  @Builder
  @Validated
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PrivilegedUser {

    @NotBlank
    private String uid;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
  }
}