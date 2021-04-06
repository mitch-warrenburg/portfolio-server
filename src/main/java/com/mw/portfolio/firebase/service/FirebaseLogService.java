package com.mw.portfolio.firebase.service;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;

import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Log4j2
@Service
public class FirebaseLogService {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");

  static {
    dateTimeFormatter.withZone(ZoneId.systemDefault());
  }

  public void logSuccess(String successType, UserRecord userRecord, FirebaseToken decodedToken) {
    log.info("{} success. [uid]: {} [phoneNumber]: {} [authTime]: {} [expiryTime]: {}", successType, userRecord.getUid(), userRecord.getUid(), claimToDateString("auth_time", decodedToken), claimToDateString("exp", decodedToken));
  }

  public void logSuccess(String successType, UserRecord userRecord) {
    log.info("{} success. [uid]: {} [phoneNumber]: {}", successType, userRecord.getUid(), userRecord.getUid());
  }

  private long getClaimEpoch(String property, FirebaseToken decodedToken) {
    return 1000 * (long) decodedToken.getClaims().get(property);
  }

  private String epochToDateString(long epoch) {
    return dateTimeFormatter.format(ofInstant(ofEpochMilli(epoch), ZoneId.systemDefault()));
  }

  private String claimToDateString(String property, FirebaseToken decodedToken) {
    return epochToDateString(getClaimEpoch(property, decodedToken));
  }
}
