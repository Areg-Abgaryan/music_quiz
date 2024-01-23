/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.manager;

import com.areg.project.QuizConstants;
import com.areg.project.model.dto.UserDTO;
import com.areg.project.model.entity.UserEntity;
import com.areg.project.service.hibernate.UserServiceHibernate;
import com.areg.project.util.UtilMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//  FIXME !! Consider adding user info to quiz mode context after authentication
//  FIXME !! Refactor this class, many duplicates
@Service
public class AuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);
    private final UserServiceHibernate userServiceHibernate;
    private final UserManager userManager;
    private final EncryptionManager encryptionManager;
    private final EmailVerificationManager emailVerificationManager;

    @Autowired
    public AuthenticationManager(UserServiceHibernate userServiceHibernate, UserManager userManager,
                                 EncryptionManager encryptionManager, EmailVerificationManager emailVerificationManager) {
        this.userServiceHibernate = userServiceHibernate;
        this.userManager = userManager;
        this.encryptionManager = encryptionManager;
        this.emailVerificationManager = emailVerificationManager;
    }

    public void authenticate() {

        System.out.print("Hey ! Welcome to Music Quiz !\n");

        boolean isUserSuccessfullyAuthenticated = false;
        do {
            System.out.print("""
                1. Log In  2. Sign Up
                Enter option : \s""");

            final var scanner = new Scanner(System.in);
            final String option = scanner.next();

            switch (option) {
                case "1" -> isUserSuccessfullyAuthenticated = logIn();
                case "2" -> isUserSuccessfullyAuthenticated = signUp();
                default -> System.out.println("Wrong input ! Please, choose one of the options above.");
            }
        } while (! isUserSuccessfullyAuthenticated);
    }

    private boolean logIn() {

        while (true) {

            final var scanner = new Scanner(System.in);
            System.out.println("E-mail : ");
            //  FIXME !! Check here to call getEmailMessage() method
            final String email = scanner.next();
            System.out.println("Password : ");
            final String password = scanner.next();

            final var user = userServiceHibernate.getUserByEmail(email);
            if (user != null) {

                //  FIXME !! Check here to call getPasswordInput() method
                if (! user.getPassword().equals(encryptionManager.encrypt(password, user.getSalt()))) {

                    System.out.println("""
                        Wrong password provided ! Do you want to recover your password ?
                        1. Yes  2. No, try to authenticate again
                        Enter option : \s""");

                    while (true) {

                        final var loopScanner = new Scanner(System.in);
                        final String choice = loopScanner.nextLine();

                        if (! UtilMethods.isOptionInValidRange(choice, 1, 2)) {
                            logger.info("Wrong option input");
                            System.out.println("Wrong input !");
                            break;
                        }

                        final int choiceInt = Integer.parseInt(choice);
                        if (choiceInt == 1) {
                            final String otp = encryptionManager.generateOneTimePassword();
                            final String otpMessage = "Your OTP is " + otp + ". It is expiring in 1 minute.";
                            emailVerificationManager.sendEmail(email, "Music Quiz OTP", otpMessage);
                            System.out.println("Please, enter the code that was sent to your " + email + " e-mail address.\n");

                            final ExecutorService executorService = Executors.newFixedThreadPool(1);
                            final String otpFromUserInput = getOTPFromUserInput(executorService);
                            if ((otpFromUserInput.equals(otp))) {
                                final String newPassword = getPasswordInput();
                                final var salt = encryptionManager.generateSalt();
                                final String newEncryptedPassword = encryptionManager.encrypt(newPassword, salt);
                                userServiceHibernate.updateUserPassword(user, salt, newEncryptedPassword);
                            }

                            executorService.shutdown();
                            break;
                        } else if (choiceInt == 2) {
                            logger.info("UserEntity {} is trying to authenticate again.", user.getUsername());
                            System.out.println("Trying to authenticate again !");
                            break;
                        }
                    }
                } else {
                    logger.info("UserEntity {} successfully logged in !", user.getUsername());
                    System.out.println("Successfully logged in !");
                    break;
                }
            } else {
                System.out.println("Wrong e-mail provided !");
                return false;
            }
        }
        return true;
    }

    private boolean signUp() {

        final var users = userServiceHibernate.getAllUsers();
        final String email = getInput("Enter e-mail : ", input -> isValidEmail(users, input));
        final String username = getInput("Enter username : ", input -> isValidUserName(users, input));
        final String password = getPasswordInput();

        final ExecutorService executorService = Executors.newFixedThreadPool(1);

        //  Create one time password & send to user's email
        final String otp = encryptionManager.generateOneTimePassword();
        final String otpMessage = "Your OTP is " + otp + ". It is expiring in 1 minute.";
        emailVerificationManager.sendEmail(email, "Music Quiz OTP", otpMessage);
        System.out.println("Please, enter the code that was sent to your " + email + " e-mail address.\n");

        //  Check whether system generated OTP & the one that user has written match
        final String otpFromUserInput = getOTPFromUserInput(executorService);
        if ((otpFromUserInput.equals(otp))) {
            final var salt = encryptionManager.generateSalt();
            final var encryptedPassword = encryptionManager.encrypt(password, salt);

            final var userDto = new UserDTO(email, username, encryptedPassword);
            userManager.signUp(userDto);
        } else {
            if (! otpFromUserInput.isEmpty()) {
                logger.info("OTP is incorrect ! Expected value : {}, user input : {}", otp, otpFromUserInput);
                System.out.println("Code is incorrect !");
            }
            System.out.println("Unsuccessful authorization !\n");
            return false;
        }

        executorService.shutdown();
        return true;
    }

    private String getInput(String prompt, Predicate<String> validationFunction) {
        while (true) {
            System.out.println(prompt);
            final var scanner = new Scanner(System.in);
            final String input = scanner.next();
            if (validationFunction.test(input)) {
                return input;
            }
        }
    }

    private String getPasswordInput() {

        printPasswordRules();
        String password;

        while (true) {
            System.out.println("Enter password : ");
            final var scanner = new Scanner(System.in);
            final String pass = scanner.next();
            if (isValidPassword(pass)) {
                System.out.println("Repeat password : ");
                final var scanner2 = new Scanner(System.in);
                final String pass2 = scanner2.next();
                if (! pass2.equals(pass)) {
                    System.out.println("Passwords do not match");
                }
                password = pass;
                break;
            }
        }
        return password;
    }

    private void printPasswordRules() {
        System.out.print("""
            Create a secure password following these rules :
            
            Password length : 8-20 characters.
            It contains at least one digit, one upper case letter, one lower case letter,
            one special character [ !@#$%&*()-+=^. ], doesn’t contain any white space.
            
            """);
    }

    private boolean isValidUserName(List<UserEntity> userEntities, String userName) {

        if (userName == null || userName.isEmpty()) {
            throw new RuntimeException("Error : Username can't be null or empty !");
        }

        if (userName.length() < 4 || userName.length() > 20) {
            System.out.println("The username length must be 4-20 characters. Choose another one !");
            return false;
        }

        for (var user : userEntities) {
            if (userName.equals(user.getUsername())) {
                System.out.println("This username is already reserved. Choose another one !");
                return false;
            }
        }
        return true;
    }

    private boolean isValidEmail(List<UserEntity> userEntities, String email) {

        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Error : E-mail can't be null or empty !");
        }

        final var regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(regexPattern);
        final Matcher matcher = pattern.matcher(email);

        if (! matcher.matches()) {
            System.out.println("E-mail format is invalid");
            return false;
        }

        for (var user : userEntities) {
            if (email.equals(user.getEmail())) {
                System.out.println("This e-mail has already been used for registration, choose another one");
                return false;
            }
        }
        return true;
    }

    private boolean isValidPassword(String password) {

        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Error : Password can't be null or empty !");
        }

        final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()-+=^.])(?=\\S+$).{8,20}$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(password);
        if (! matcher.matches()) {
            System.out.println("Invalid password, choose another");
            return false;
        }
        return true;
    }

    private String getOTPFromUserInput(ExecutorService executorService) {

        final Callable<String> callable = () -> new Scanner(System.in).nextLine();
        final Future<String> inputFuture = executorService.submit(callable);

        try {
            return inputFuture.get(QuizConstants.OTPTimeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Thread was interrupted", e);
        } catch (TimeoutException e) {
            logger.info("UserEntity timed out to enter OTP !");
            System.out.println("\nTimed out to enter code !");
        }

        return "";
    }
}
