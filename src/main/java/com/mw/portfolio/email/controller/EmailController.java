package com.mw.portfolio.email.controller;

import com.mw.portfolio.email.model.EmailRequest;
import com.mw.portfolio.email.model.EmailResponse;
import com.mw.portfolio.email.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {

  private final EmailService emailService;

  @PostMapping("/send")
  public EmailResponse sendEmail(@Valid @RequestBody EmailRequest email) {
    return emailService.sendEmail(email);
  }
}
