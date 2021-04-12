package com.mw.portfolio.scheduling.converter;

import com.mw.portfolio.scheduling.entity.ScheduledEvent;
import com.mw.portfolio.scheduling.model.ScheduledEventRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ScheduledEventJsonToScheduledEventConverter implements Converter<ScheduledEventRequest, ScheduledEvent> {

  @Override
  public ScheduledEvent convert(ScheduledEventRequest event) {
    return ScheduledEvent.builder()
        .uid(event.getUid())
        .endDate(event.getEnd())
        .startDate(event.getStart())
        .build();
  }
}
