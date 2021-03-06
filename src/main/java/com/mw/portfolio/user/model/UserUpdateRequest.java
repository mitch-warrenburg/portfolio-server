package com.mw.portfolio.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

  @NotBlank
  private String uid;
  private String email;
  private String company;
  private String username;
}
