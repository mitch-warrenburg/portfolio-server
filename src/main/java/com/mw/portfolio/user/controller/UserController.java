package com.mw.portfolio.user.controller;

import com.mw.portfolio.user.entity.User;
import com.mw.portfolio.user.model.UserUpdateRequest;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserService userService;

  @PutMapping
  public User updateUser(@Valid @RequestBody UserUpdateRequest request) {
    return userService.updateUser(request);
  }
}
