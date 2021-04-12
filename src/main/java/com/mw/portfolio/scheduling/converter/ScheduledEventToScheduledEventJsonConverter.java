package com.mw.portfolio.scheduling.converter;

import static java.util.Optional.ofNullable;

import com.mw.portfolio.scheduling.entity.ScheduledEvent;
import com.mw.portfolio.scheduling.model.ScheduledEventJson;
import com.mw.portfolio.scheduling.model.ScheduledEventJson.ExtendedProps;
import com.mw.portfolio.security.model.FirebaseAuthentication;
import lombok.val;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ScheduledEventToScheduledEventJsonConverter implements Converter<ScheduledEvent, ScheduledEventJson> {

  private static final String OTHER_USER_EVENT_BG_COLOR = "rgba(255, 59, 71, 0.3)";
  private static final String OTHER_USER_EVENT_BORDER_COLOR = "rgba(255, 59, 71, 1)";
  private static final String CURRENT_USER_EVENT_BG_COLOR = "rgba(58, 109, 240, 0.3)";
  private static final String CURRENT_USER_EVENT_BORDER_COLOR = "rgba(58, 109, 240, 1)";

  @Override
  public ScheduledEventJson convert(ScheduledEvent event) {

    val isCurrentUserEvent = ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .filter(authentication -> authentication instanceof FirebaseAuthentication)
        .map(authentication -> (FirebaseAuthentication) authentication)
        .map(FirebaseAuthentication::getName)
        .map(uid -> event.getUid().equals(uid))
        .orElse(false);

    return ScheduledEventJson.builder()
        .id(event.getId())
        .end(event.getEndDate())
        .start(event.getStartDate())
        .extendedProps(new ExtendedProps(isCurrentUserEvent))
        .backgroundColor(isCurrentUserEvent ? CURRENT_USER_EVENT_BG_COLOR : OTHER_USER_EVENT_BG_COLOR)
        .borderColor(isCurrentUserEvent ? CURRENT_USER_EVENT_BORDER_COLOR : OTHER_USER_EVENT_BORDER_COLOR)
        .build();
  }
}
