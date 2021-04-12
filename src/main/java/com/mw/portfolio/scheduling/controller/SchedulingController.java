package com.mw.portfolio.scheduling.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.mw.portfolio.scheduling.model.ScheduledEventJson;
import com.mw.portfolio.scheduling.model.ScheduledEventRequest;
import com.mw.portfolio.scheduling.service.SchedulingService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/scheduling")
public class SchedulingController {

  private final SchedulingService schedulingService;

  @GetMapping("/events")
  public List<ScheduledEventJson> getEvents() {
    return schedulingService.getScheduledEvents();
  }

  @ResponseStatus(CREATED)
  @PostMapping("/events")
  public void createScheduledEvent(@Valid @RequestBody ScheduledEventRequest event) {
    schedulingService.createScheduledEvent(event);
  }

  @ResponseStatus(NO_CONTENT)
  @DeleteMapping("/events/{eventId}")
  public void deleteScheduledEvent(@PathVariable Long eventId) {
    schedulingService.deleteScheduledEvent(eventId);
  }
}
