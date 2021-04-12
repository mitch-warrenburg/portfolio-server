package com.mw.portfolio.scheduling.service;


import static com.mw.portfolio.config.security.Constants.USER_RESOURCE_MISMATCH_EXCEPTION_MSG;
import static java.util.stream.Collectors.toList;

import com.mw.portfolio.scheduling.converter.ScheduledEventJsonToScheduledEventConverter;
import com.mw.portfolio.scheduling.converter.ScheduledEventToScheduledEventJsonConverter;
import com.mw.portfolio.scheduling.model.ScheduledEventJson;
import com.mw.portfolio.scheduling.model.ScheduledEventRequest;
import com.mw.portfolio.scheduling.repository.ScheduledEventRepository;
import com.mw.portfolio.security.model.FirebaseAuthentication;
import com.mw.portfolio.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SchedulingService {

  private final UserService userService;
  private final ScheduledEventRepository repository;
  private final ScheduledEventToScheduledEventJsonConverter toJsonConverter;
  private final ScheduledEventJsonToScheduledEventConverter fromJsonConverter;

  @SuppressWarnings("ConstantConditions")
  @PreAuthorize("#event.uid == authentication.name")
  public void createScheduledEvent(@P("event") ScheduledEventRequest event) {
    userService.updateEmail(event.getUid(), event.getEmail());
    repository.saveAndFlush(fromJsonConverter.convert(event));
  }

  public void deleteScheduledEvent(Long eventId) {

    val event = repository.findById(eventId)
        .orElseThrow(EntityNotFoundException::new);

    val auth = (FirebaseAuthentication) SecurityContextHolder.getContext().getAuthentication();

    if (!auth.getName().equals(event.getUid())) {
      throw new InsufficientAuthenticationException(USER_RESOURCE_MISMATCH_EXCEPTION_MSG);
    }

    repository.delete(event);
  }

  public List<ScheduledEventJson> getScheduledEvents() {
    return repository.findAll().stream()
        .map(toJsonConverter::convert)
        .collect(toList());
  }
}
