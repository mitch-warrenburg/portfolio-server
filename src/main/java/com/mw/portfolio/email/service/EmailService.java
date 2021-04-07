package com.mw.portfolio.email.service;

import static com.sendgrid.Method.POST;
import static java.lang.String.format;

import com.mw.portfolio.email.exception.EmailCreationException;
import com.mw.portfolio.email.exception.EmailSendException;
import com.mw.portfolio.email.exception.MaxEmailCountException;
import com.mw.portfolio.email.model.EmailRequest;
import com.mw.portfolio.email.model.EmailResponse;
import com.mw.portfolio.user.entity.User;
import com.mw.portfolio.user.service.UserService;
import com.sendgrid.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class EmailService {

  private static final int MAX_EMAIL_COUNT = 25;
  private static final String ENDPOINT = "mail/send";
  private static final String CONTENT_TYPE = "text/plain";
  private static final String EMAIL_SUBJECT_FORMAT = "%s: %s";
  private static final String SUCCESS_MESSAGE = "Successfully submitted email to external API.";
  private static final String EMAIL_BODY_FORMAT = "<p>Company: %s\nName: %s\nEmail: %s\nPhone: %s\n\n\n%s</p><br/><br/>";

  private final SendGrid sendGrid;
  private final Email senderEmail;
  private final Email recipientEmail;
  private final UserService userService;

  public EmailResponse sendEmail(EmailRequest request) {

    val user = getUser(request.getUid());
    val subject = format(EMAIL_SUBJECT_FORMAT, user.getCompany(), user.getUsername());
    val body = format(EMAIL_BODY_FORMAT, user.getCompany(), user.getUsername(), request.getEmail(), user.getPhoneNumber(), request.getContent());

    try {

      val response = sendGrid.api(buildRequest(user, request, subject, body));
      user.setEmail(request.getEmail());
      user.setEmailCount(user.getEmailCount() + 1);
      userService.persistAuthorizedUser(user);

      log.info("Successfully sent email. [status]: {} [uid]: {}", response.getStatusCode(), user.getUid());

      return EmailResponse.builder()
          .message(SUCCESS_MESSAGE)
          .status(response.getStatusCode())
          .build();

    } catch (IOException e) {
      throw new EmailSendException(e);
    }
  }

  private Request buildRequest(User user, EmailRequest emailRequest, String subject, String body) {

    try {
      val userCC = new Personalization();
      val userEmail = new Email(emailRequest.getEmail(), user.getUsername());
      val mail = new Mail(senderEmail, subject, recipientEmail, new Content(CONTENT_TYPE, body));

      userCC.addCc(userEmail);
      userCC.setSubject(subject);
      userCC.addTo(recipientEmail);
      mail.setReplyTo(recipientEmail);
      mail.addPersonalization(userCC);

      val request = new Request();

      request.setMethod(POST);
      request.setEndpoint(ENDPOINT);
      request.setBody(mail.build());

      return request;
    } catch (IOException e) {
      throw new EmailCreationException(e);
    }
  }

  private User getUser(String uid) {
    val user = userService.getAuthorizedUser(uid);

    if (user.getEmailCount() >= MAX_EMAIL_COUNT) {
      throw new MaxEmailCountException(format("Maximum email count reach for user.  A user may send no more than %s emails.", MAX_EMAIL_COUNT));
    }

    return user;
  }
}
