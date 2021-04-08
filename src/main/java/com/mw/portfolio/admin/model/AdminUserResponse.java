package com.mw.portfolio.admin.model;

import lombok.*;

import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponse {
  private String uid;
  private String username;

  @Singular
  private List<String> roles;
}
