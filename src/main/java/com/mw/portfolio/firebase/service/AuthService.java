package com.mw.portfolio.firebase.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.mw.portfolio.security.model.FirebaseUserDetails;

import javax.servlet.http.Cookie;

public interface AuthService {
  FirebaseUserDetails authenticate(String authToken) throws FirebaseAuthException;

  FirebaseUserDetails verifySession(String cookie) throws FirebaseAuthException;

  Cookie createSessionCookie(String authToken);
}
