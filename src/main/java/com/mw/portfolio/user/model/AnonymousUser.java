package com.mw.portfolio.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnonymousUser {

  private String sessionId;
  private String name;
  private String email;
  private String phoneNumber;
}
