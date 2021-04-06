package com.mw.portfolio;

import static com.mw.portfolio.util.KeyFileUtil.writeGcpKeyFile;
import static java.util.TimeZone.getTimeZone;

import com.mw.portfolio.config.email.EmailProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@Log4j2
@AllArgsConstructor
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "dbAuditor")
public class PortfolioApplication {

  private final EmailProperties properties;

  public static void main(String[] args) {
    TimeZone.setDefault(getTimeZone("America/Los_Angeles"));
    writeGcpKeyFile();
    SpringApplication.run(PortfolioApplication.class, args);
  }

  @SneakyThrows
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
