package com.mw.portfolio.scheduling.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "scheduled_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@TypeDef(name = "psqlEnum", typeClass = PostgreSQLEnumType.class)
public class ScheduledEvent {

  @Id
  @JsonIgnore
  @GeneratedValue(strategy = SEQUENCE, generator = "authenticated_user_id_seq")
  @SequenceGenerator(name = "authenticated_user_id_seq", sequenceName = "authenticated_user_id_seq", allocationSize = 1)
  private Long id;

  private String uid;
  private String endDate;
  private String startDate;
}
