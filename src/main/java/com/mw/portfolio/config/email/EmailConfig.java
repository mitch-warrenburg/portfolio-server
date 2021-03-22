package com.mw.portfolio.config.email;

import com.sendgrid.Email;
import com.sendgrid.SendGrid;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class EmailConfig {

  private final EmailProperties properties;

  @Bean
  public SendGrid sendGrid() {
    return new SendGrid(properties.getApiKey());
  }

  @Bean
  public Email senderEmail() {
    return new Email(properties.getAddress());
  }
}
