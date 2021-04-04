package com.mw.portfolio.user.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import com.mw.portfolio.config.db.AuditedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authenticated_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class User extends AuditedEntity<String> implements Serializable {

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "authenticated_user_id_seq")
  @SequenceGenerator(name = "authenticated_user_id_seq", sequenceName = "authenticated_user_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "uid")
  private String uid;

  private String username;
  private String email;
  private String company;
  private String phoneNumber;
  private int emailCount;
}
