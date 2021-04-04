package com.mw.portfolio.metadata.entity;

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
@Table(name = "client_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ClientMetadata extends AuditedEntity<String> implements Serializable {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = SEQUENCE, generator = "client_metadata_id_seq")
  @SequenceGenerator(name = "client_metadata_id_seq", sequenceName = "client_metadata_id_seq", allocationSize = 1)
  private Long id;
  private boolean ios;
  private boolean ipad;
  private boolean iphone;
  private boolean mobile;
  private String fingerprint;
  private String os;
  private String osVersion;
  private String browser;
  private String browserVersion;
  private String browserFullVersion;
  private String device;
  private String deviceCpu;
  private String deviceType;
  private String deviceVendor;
  private String timezone;
  private String language;
  private String systemLanguage;
  private String cookies;
  private String localStorage;
  private String sessionStorage;
  private String resolution;
  private String availableResolution;
  private String screenInfo;
}
