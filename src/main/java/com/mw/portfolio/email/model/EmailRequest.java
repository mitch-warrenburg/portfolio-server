package com.mw.portfolio.email.model;

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
public class EmailRequest {

  @NotBlank
  private String name;

  @Email
  private String address;

  @NotBlank
  private String company;

  @NotBlank
  private String content;

  @Builder.Default
  private String phoneNumber = "";
}
