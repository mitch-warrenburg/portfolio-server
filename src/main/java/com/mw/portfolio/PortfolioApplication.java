package com.mw.portfolio;

import com.mw.portfolio.config.email.EmailProperties;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Log4j2
@AllArgsConstructor
@SpringBootApplication
public class PortfolioApplication {

  private final EmailProperties properties;

  public static void main(String[] args) {
    SpringApplication.run(PortfolioApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void printEnv() {
    val sender = properties.getSender();
    val recipient = properties.getRecipient();

    log.info("-------------------------- Application Configuration --------------------------");
    log.info("[apiKey]: {}", properties.getApiKey());
    log.info("[senderName]: {} [senderAddress]: {}", sender.getName(), sender.getAddress());
    log.info("[recipientName]: {} [recipientAddress]: {}", recipient.getName(), recipient.getAddress());
    log.info("-------------------------------------------------------------------------------");
  }
}
