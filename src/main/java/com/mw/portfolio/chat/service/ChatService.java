package com.mw.portfolio.chat.service;

import com.mw.portfolio.chat.modal.ChatUserResponse;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatService {

  private final UserService userService;

  public ChatUserResponse getDefaultChatUser() {

    val user = userService.getDefaultChatUser();

    return ChatUserResponse.builder()
        .uid(user.getUid())
        .username(user.getUsername())
        .build();
  }
}
