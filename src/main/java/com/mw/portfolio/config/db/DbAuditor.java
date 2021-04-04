package com.mw.portfolio.config.db;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class DbAuditor implements AuditorAware<String> {

  private static final String DEFAULT_AUDITOR = "SYSTEM";

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(DEFAULT_AUDITOR);
  }
}
