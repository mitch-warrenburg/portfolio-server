package com.mw.portfolio.security.service;

import com.mw.portfolio.security.model.FirebaseUserDetails;
import org.springframework.http.ResponseCookie;

public interface AuthenticationService {
  FirebaseUserDetails authenticate(String authToken);

  FirebaseUserDetails verifySession(String cookie);

  ResponseCookie createSessionCookie(String authToken);
}
