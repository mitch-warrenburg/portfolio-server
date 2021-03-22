package com.mw.portfolio.config.email;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Component
@Validated
@NoArgsConstructor
@ConfigurationProperties("email")
public class EmailProperties {

  @NotBlank
  private String address;

  @NotBlank
  private String apiKey;
}
