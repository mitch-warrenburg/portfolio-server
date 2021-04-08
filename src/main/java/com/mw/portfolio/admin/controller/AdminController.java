package com.mw.portfolio.admin.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.mw.portfolio.admin.model.AdminUserResponse;
import com.mw.portfolio.admin.service.AdminService;
import com.mw.portfolio.chat.modal.ChatUserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

  private final AdminService adminService;

  @PostMapping("/chat/login")
  @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
  public ChatUserResponse getChatUserAsAdmin() throws FirebaseAuthException {
    return adminService.getChatUserAsAdmin();
  }

  @GetMapping("/users/{uid}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
  public AdminUserResponse getUserAsAdmin(@PathVariable String uid) {
    return adminService.getUserAsAdmin(uid);
  }
}
