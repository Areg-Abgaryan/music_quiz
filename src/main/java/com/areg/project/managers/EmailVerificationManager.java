/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationManager {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationManager.class);
    private final JavaMailSender mailSender;

    public EmailVerificationManager(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    public void sendEmail(String emailAddress, String subject, String text) {
        //  FIXME !! Handle the case when email address is empty or MailServerAddress is empty
        final SimpleMailMessage message = createMailMessage(emailAddress, subject, text);
        mailSender.send(message);
        logger.info("E-mail was sent to the {} address", emailAddress);
    }

    private SimpleMailMessage createMailMessage(String emailAddress, String subject, String text) {
        final var message = new SimpleMailMessage();
        message.setFrom(QuizConstants.MailServerAddress);
        message.setTo(emailAddress);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}