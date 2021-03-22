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

  private final Email senderEmail;
  private final SendGrid sendGrid;

  public EmailResponse sendEmail(EmailRequest emailRequest) {

    val email = trimAllWhitespace(emailRequest);
    val subject = format(EMAIL_BODY_FORMAT, email.getCompany(), email.getName());
    val emailBody = format(EMAIL_SUBJECT_FORMAT, email.getCompany(), email.getName(), email.getAddress(), email.getPhoneNumber(), email.getContent());

    try {

      val response = sendGrid.api(buildRequest(email.getAddress(), subject, emailBody));

      log.info("Successfully sent email. [status]: {}", response.getStatusCode());

      return EmailResponse.builder()
          .message(SUCCESS_MESSAGE)
          .status(response.getStatusCode())
          .build();

    } catch (IOException e) {
      throw new EmailSendException(e);
    }
  }

  private Request buildRequest(String email, String subject, String content) {

    try {
      val request = new Request();
      val mail = new Mail(senderEmail, subject, new Email(email), new Content(CONTENT_TYPE, content));

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
