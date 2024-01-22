/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.manager;

import com.areg.project.QuizConstants;
import org.apache.commons.lang3.StringUtils;
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

        if (StringUtils.isBlank(emailAddress)) {
            logger.info("E-mail address is null or empty.");
            return;
        }

        if (StringUtils.isBlank(text)) {
            logger.info("E-mail text is null or empty.");
            return;
        }

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