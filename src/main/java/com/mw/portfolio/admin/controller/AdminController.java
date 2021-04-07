package com.mw.portfolio.admin.controller;

import com.mw.portfolio.admin.service.AdminService;
import com.mw.portfolio.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

  private final AdminService adminService;

  @PostMapping("/auth")
  @PreAuthorize("hasRole('ADMIN')")
  public User getAuthenticatedAdminUser() {
    return adminService.getAuthenticatedAdminUser();
  }

  @GetMapping("/roles/{uid}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
  public List<String> getRolesForUser(@PathVariable String uid) {
    return adminService.getRolesForUser(uid);
  }
}
