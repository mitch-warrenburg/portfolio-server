package com.mw.portfolio;

import static com.mw.portfolio.util.KeyFileUtil.writeGcpKeyFile;
import static java.util.TimeZone.getTimeZone;

import com.mw.portfolio.config.email.EmailProperties;
import com.mw.portfolio.config.security.SecurityProperties;
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

  private final EmailProperties emailProperties;
  private final SecurityProperties securityProperties;

  public static void main(String[] args) {
    writeGcpKeyFile();
    TimeZone.setDefault(getTimeZone("America/Los_Angeles"));
    SpringApplication.run(PortfolioApplication.class, args);
  }

  @SneakyThrows
  @EventListener(ApplicationReadyEvent.class)
  public void printEnv() {
    val sender = emailProperties.getSender();
    val recipient = emailProperties.getRecipient();
    val admin = securityProperties.getAdmin();
    val system = securityProperties.getSystem();

    log.info("-------------------------- Application Configuration --------------------------");
    log.info("[apiKey]: {}", emailProperties.getApiKey());
    log.info("[senderName]: {} [senderAddress]: {}", sender.getName(), sender.getAddress());
    log.info("[recipientName]: {} [recipientAddress]: {}", recipient.getName(), recipient.getAddress());
    log.info("[chatSessionId]: {}", securityProperties.getChatSessionId());
    log.info("[adminUid]: {} [adminUsername]: {} [adminPassword]: {}", admin.getUid(), admin.getUsername(), admin.getPassword());
    log.info("[systemUid]: {} [systemUsername]: {} [systemPassword]: {}", system.getUid(), system.getUsername(), system.getPassword());
    log.info("-------------------------------------------------------------------------------");
  }
}
