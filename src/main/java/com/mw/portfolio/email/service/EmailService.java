package com.mw.portfolio.email.service;

import static com.sendgrid.Method.POST;
import static java.lang.String.format;

import com.mw.portfolio.email.exception.EmailCreationException;
import com.mw.portfolio.email.exception.EmailSendException;
import com.mw.portfolio.email.model.EmailRequest;
import com.mw.portfolio.email.model.EmailResponse;
import com.sendgrid.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Log4j2
@Service
@AllArgsConstructor
public class EmailService {

  private static final String ENDPOINT = "mail/send";
  private static final String CONTENT_TYPE = "text/plain";
  private static final String SUCCESS_MESSAGE = "Successfully submitted email to external API.";

  private static final String EMAIL_BODY_FORMAT = "%s: %s";
  private static final String EMAIL_SUBJECT_FORMAT = "Company: %s\nName: %s\nEmail: %s\nPhone: %s\n\n\n%s";

  private final SendGrid sendGrid;
  private final Email senderEmail;
  private final Email recipientEmail;

  public EmailResponse sendEmail(EmailRequest emailRequest) {

    val request = trimAllWhitespace(emailRequest);
    val subject = format(EMAIL_BODY_FORMAT, request.getCompany(), request.getName());
    val body = format(EMAIL_SUBJECT_FORMAT, request.getCompany(), request.getName(), request.getAddress(), request.getPhoneNumber(), request.getContent());

    try {

      val response = sendGrid.api(buildRequest(request, subject, body));

      log.info("Successfully sent email. [status]: {}", response.getStatusCode());

      return EmailResponse.builder()
          .message(SUCCESS_MESSAGE)
          .status(response.getStatusCode())
          .build();

    } catch (IOException e) {
      throw new EmailSendException(e);
    }
  }

  private Request buildRequest(EmailRequest emailRequest, String subject, String body) {

    try {
      val userCC = new Personalization();
      val userEmail = new Email(emailRequest.getAddress(), emailRequest.getName());
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

  private EmailRequest trimAllWhitespace(EmailRequest email) {
    return EmailRequest.builder()
        .name(email.getName().trim())
        .company(email.getCompany().trim())
        .address(email.getAddress().trim())
        .content(email.getContent().trim())
        .phoneNumber(email.getPhoneNumber().trim())
        .build();
  }
}
