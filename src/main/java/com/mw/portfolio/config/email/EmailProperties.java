package com.mw.portfolio.config.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Component
@Validated
@NoArgsConstructor
@ConfigurationProperties("email")
public class EmailProperties {

  @NotBlank
  private String apiKey;

  @Valid
  public EmailDetails sender;

  @Valid
  public EmailDetails recipient;

  @Data
  @Builder
  @Validated
  @NoArgsConstructor
  @AllArgsConstructor
  public static class EmailDetails {

    @NotBlank
    private String name;

    @Email
    private String address;
  }
}
