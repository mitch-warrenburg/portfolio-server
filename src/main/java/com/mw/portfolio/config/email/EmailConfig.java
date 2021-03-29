package com.mw.portfolio.config.email;

import com.sendgrid.Email;
import com.sendgrid.SendGrid;
import lombok.AllArgsConstructor;
import lombok.val;
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
    val sender = properties.getSender();
    return new Email(sender.getAddress(), sender.getName());
  }

  @Bean
  public Email recipientEmail() {
    val recipient = properties.getRecipient();
    return new Email(recipient.getAddress(), recipient.getName());
  }
}
