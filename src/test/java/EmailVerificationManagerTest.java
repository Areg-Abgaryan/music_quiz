/**
 * Copyright (c) 2023 Areg Abgaryan
 */

/*
import com.areg.project.config.MailSenderConfig;
import com.areg.project.logging.LogMachine;
import com.areg.project.managers.EmailVerificationManager;
import jakarta.mail.internet.MimeMessage;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.stereotype.Service;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;

@ContextConfiguration(classes = {EmailVerificationManager.class, JavaMailSender.class})
public class EmailVerificationManagerTest extends AbstractTestNGSpringContextTests {

    private EmailVerificationManager emailVerificationManager;

    @Autowired
    private JavaMailSender javaMailSender;

    //  FIXME !! Figure out this class
    private static final LogMachine logMachine = new LogMachine(EmailVerificationManager.class);

    @BeforeTest
    void setUp(JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
        //javaMailSender = Mockito.spy(JavaMailSenderImpl.class);
        emailVerificationManager = new EmailVerificationManager(javaMailSender);
    }

    @Test
    void testSendEmail() {
        final String email = "someone@gmail.com";
        final String subject = "subject";
        final String text = "text";

        emailVerificationManager.sendEmail(email, subject, text);

        try {
            Mockito.verify(javaMailSender).send((MimeMessage) any());
        } catch (WantedButNotInvoked we) {
            Assert.fail("javaMailSender#send method is not called.");
        }
    }

    @Test
    void testSendEmailWhenEmailIsNullOrEmpty() {

        final String subject = "subject";
        final String text = "text";

        emailVerificationManager.sendEmail(null, subject, text);

        // Verify that the mailSender was not called
        try {
            Mockito.verify(javaMailSender, Mockito.atLeastOnce()).send((MimeMessage) any());
        } catch (WantedButNotInvoked we) {
            logger.info("javaMailSender#send method is not called.");
        }

        emailVerificationManager.sendEmail("", subject, text);

        // Verify that the mailSender was not called
        try {
            Mockito.verify(javaMailSender, Mockito.atLeastOnce()).send((MimeMessage) any());
        } catch (WantedButNotInvoked we) {
            logger.info("javaMailSender#send method is not called.");
        }

        emailVerificationManager.sendEmail("aabgaryan@vmware.com", subject, text);
        try {
            Mockito.verify(javaMailSender, Mockito.atLeastOnce()).send((MimeMessage) any());
        } catch (WantedButNotInvoked we) {
            logger.info("javaMailSender#send method is not called.");
        }
    }

    @Test
    void testSendEmailWhenTextIsNullOrEmpty() {

        final String emailAddress = "emailAddress";
        final String subject = "subject";

        emailVerificationManager.sendEmail(emailAddress, subject, null);

        // Verify that the mailSender was not called
        try {
            Mockito.verify(javaMailSender, Mockito.atLeastOnce()).send((MimeMessage) any());
        } catch (WantedButNotInvoked we) {
            logger.info("javaMailSender#send method is not called.");
        }

        emailVerificationManager.sendEmail(emailAddress, subject, "");

        // Verify that the mailSender was not called
        try {
            Mockito.verify(javaMailSender, Mockito.atLeastOnce()).send((MimeMessage) any());
        } catch (WantedButNotInvoked we) {
            logger.info("javaMailSender#send method is not called.");
        }
    }

    @Test
    void testSendEmailWhenOtpIsMissing() {

    }
}*/
