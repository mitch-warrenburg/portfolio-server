package com.mw.portfolio.config.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class AuditedEntity<U> {

  @JsonIgnore
  @CreatedBy
  @Column(name = "created_by")
  private U createdBy;

  @JsonIgnore
  @LastModifiedBy
  @Column(name = "modified_by")
  private U modifiedBy;

  @JsonIgnore
  @CreatedDate
  @Column(name = "created_ts")
  private Date createdTs;

  @JsonIgnore
  @LastModifiedDate
  @Column(name = "modified_ts")
  private Date modifiedTs;
}
