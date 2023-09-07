/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

//import lombok.AllArgsConstructor;

/*import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
*/
//@AllArgsConstructor
//public class EmailVerificationManager {

    /*
    private String email;
    private String myHash;

    public void sendMail() {
        // Enter the email address and password for the account from which verification link will be sent
        String email = "*****";
        String password = "*****";

        Properties theProperties = new Properties();

        theProperties.put("mail.smtp.auth", "true");
        theProperties.put("mail.smtp.starttls.enable", "true");
        theProperties.put("mail.smtp.host", "smtp.gmail.com");
        theProperties.put("mail.smtp.port", "587");

        var session = Session.getDefaultInstance(theProperties, new javax.mail.Authenticator() {
            private PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        try {
            var message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.email));
            message.setSubject("Email Verification Link");
            message.setText("Click this link to confirm your email address and complete setup for your account."
                    + "\n\nVerification Link: " + "http://localhost:8080/EmailVerification/ActivateAccount?key1=" + this.email + "&key2=" + myHash);

            Transport.send(message);
            System.out.println("Successfully sent Verification Link");
        } catch (Exception e) {
            System.out.println("Error at SendingEmail.java: " + e);
        }
    }*/
//}


/*
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.Transport;

public class EmailVerificationManager
{
    public static void start() {
        // email ID of Recipient.
        String recipient = "abgaryan.areg@gmail.com";

        // email ID of  Sender.
        String sender = "sender@gmail.com";

        // using host as localhost
        String host = "127.0.0.1";

        // Getting system properties
        Properties properties = System.getProperties();

        // Setting up mail server
        properties.setProperty("mail.smtp.host", host);

        // creating session object to get properties
        var session = Session.getDefaultInstance(properties);

        try
        {
            // MimeMessage object.
            var message = new MimeMessage(session);

            // Set From Field: adding senders email to from field.
            message.setFrom(new InternetAddress(sender));

            // Set To Field: adding recipient's email to from field.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Set Subject: subject of the email
            message.setSubject("This is Subject");

            // set body of the email.
            message.setText("This is a test mail");

            // Send email.
            Transport.send(message);
            System.out.println("Mail successfully sent");
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
*/

