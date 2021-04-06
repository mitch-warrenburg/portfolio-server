package com.mw.portfolio.user.entity;

import static com.mw.portfolio.security.model.PrincipalRole.ROLE_ANONYMOUS;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mw.portfolio.config.db.AuditedEntity;
import com.mw.portfolio.security.model.PrincipalRole;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authenticated_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@TypeDef(name = "psqlEnum", typeClass = PostgreSQLEnumType.class)
public class User extends AuditedEntity<String> implements Serializable {

  @Id
  @JsonIgnore
  @GeneratedValue(strategy = SEQUENCE, generator = "authenticated_user_id_seq")
  @SequenceGenerator(name = "authenticated_user_id_seq", sequenceName = "authenticated_user_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "uid")
  private String uid;

  @JsonIgnore
  @Enumerated(STRING)
  @Type(type = "psqlEnum")
  @Builder.Default
  private PrincipalRole role = ROLE_ANONYMOUS;

  private String email;
  private String company;
  private String username;
  private String phoneNumber;
  private int emailCount;
}
