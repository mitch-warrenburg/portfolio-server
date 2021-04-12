package com.mw.portfolio.scheduling.repository;

import com.mw.portfolio.scheduling.entity.ScheduledEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledEventRepository extends JpaRepository<ScheduledEvent, Long> {

}
