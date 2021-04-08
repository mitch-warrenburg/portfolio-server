package com.mw.portfolio.chat.modal;

import lombok.*;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChatUserResponse {
  private String uid;
  private String token;
  private String username;
  private String sessionId;
}
