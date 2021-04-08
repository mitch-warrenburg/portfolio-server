package com.mw.portfolio.chat.controller;

import com.mw.portfolio.chat.modal.ChatUserResponse;
import com.mw.portfolio.chat.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

  private final ChatService chatService;

  @GetMapping("/default-user")
  public ChatUserResponse getDefaultChatUser() {
    return chatService.getDefaultChatUser();
  }
}
