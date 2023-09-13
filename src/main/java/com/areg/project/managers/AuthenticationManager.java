/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;
import com.areg.project.entities.User;
import com.areg.project.utils.UtilMethods;
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
    private final UserManager userManager;
    private final PasswordSecurityManager passwordSecurityManager;
    private final EmailVerificationManager emailVerificationManager;

    @Autowired
    public AuthenticationManager(UserManager userManager, PasswordSecurityManager passwordSecurityManager,
                                 EmailVerificationManager emailVerificationManager) {
        this.userManager = userManager;
        this.passwordSecurityManager = passwordSecurityManager;
        this.emailVerificationManager = emailVerificationManager;
    }

    public void authenticate() {

        //  FIXME !! Remove this after db fix
        /*final var artist = new Artist("Cardie B");
        final var artistManager = new ArtistManager();
        artistManager.createArtist(artist);

        var albumFirst = new Album("album1", artist, (short) 2012, (byte) 13, "43:57", null, QuizDifficulty.EASY);
        var albumSecond = new Album("album2", artist, (short) 2015, (byte) 10, "41:29", null, QuizDifficulty.EASY);

        var albumManager = new AlbumManager();
        albumManager.createAlbum(albumFirst, artist);
        albumManager.createAlbum(albumSecond, artist);

        final var songManager = new SongManager();
        final var songFirstAlbumFirst = new Song("Song1", artist, albumFirst, "3:05", (byte) 1);
        final var songFirstAlbumSecond = new Song("Song2", artist, albumFirst, "3:16", (byte) 1);
        final var songSecondAlbumFirst = new Song("Song3", artist, albumSecond, "3:27", (byte) 1);
        final var songSecondAlbumSecond = new Song("Song4", artist, albumSecond, "3:38", (byte) 1);

        songManager.createSong(songFirstAlbumFirst,albumFirst,artist);
        songManager.createSong(songFirstAlbumSecond,albumFirst,artist);
        songManager.createSong(songSecondAlbumFirst,albumSecond,artist);
        songManager.createSong(songSecondAlbumSecond,albumSecond,artist);
    */

        System.out.print("""
                Hey ! Welcome to Music Quiz !
                1. Log In
                2. Sign Up
                Enter option : \s""");

        final var scanner = new Scanner(System.in);
        String option = scanner.next();

        boolean isInputValid = false;
        do {
            switch (option) {
                case "1" -> {
                    logIn();
                    isInputValid = true;
                }
                case "2" -> {
                    signUp();
                    isInputValid = true;
                }
                default -> {
                    System.out.println("Wrong input ! Please, choose one of the options above.");
                    option = scanner.next();
                }
            }
        } while (! isInputValid);
    }

    private void logIn() {

        while (true) {

            final var scanner = new Scanner(System.in);
            System.out.println("E-mail : ");
            final String email = scanner.next();
            System.out.println("Password : ");
            final String password = scanner.next();

            final var user = userManager.getUserByEmail(email);
            if (user != null) {

                if (! user.getPassword().equals(passwordSecurityManager.encrypt(password, user.getPasswordSalt()))) {

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
                            final String otp = passwordSecurityManager.generateOneTimePassword();
                            final String otpMessage = "Your OTP is " + otp + ". It is expiring in 1 minute.";
                            emailVerificationManager.sendEmail(email, "vmware subject", otpMessage);
                            System.out.println("Please, enter the code that was sent to your " + email + " e-mail address.\n");

                            final ExecutorService executorService = Executors.newFixedThreadPool(1);
                            final String otpFromUserInput = getOTPFromUserInput(executorService);
                            if ((otpFromUserInput.equals(otp))) {
                                final String newPassword = getPasswordInput();
                                final var salt = passwordSecurityManager.generateSalt();
                                final String newEncryptedPassword = passwordSecurityManager.encrypt(newPassword, salt);
                                userManager.updateUserPassword(user, salt, newEncryptedPassword);
                            }

                            executorService.shutdown();
                            break;
                        } else if (choiceInt == 2) {
                            logger.info("User {} is trying to authenticate again.", user.getUsername());
                            System.out.println("Trying to authenticate again !");
                            break;
                        }
                    }
                } else {
                    logger.info("User {} successfully logged in !", user.getUsername());
                    System.out.println("Successfully logged in !");
                    break;
                }
            } else {
                System.out.println("Wrong e-mail provided !");
                //  FIXME!! Here add some return code so that not authenticated user can't start quiz
                return;
            }
        }
    }

    private void signUp() {

        final var users = userManager.getAllUsers();
        final String email = getInput("Enter e-mail : ", input -> isValidEmail(users, input));
        final String userName = getInput("Enter username : ", input -> isValidUserName(users, input));
        final String password = getPasswordInput();

        final ExecutorService executorService = Executors.newFixedThreadPool(1);

        //  FIXME !! This is not working if the cycle is being stubbed for the second time in else case
        //  FIXME !! Add else case for if
        final String otp = passwordSecurityManager.generateOneTimePassword();
        final String otpMessage = "Your OTP is " + otp + ". It is expiring in 1 minute.";
        emailVerificationManager.sendEmail(email, "Music Quiz OTP", otpMessage);
        System.out.println("Please, enter the code that was sent to your " + email + " e-mail address.\n");

        final String otpFromUserInput = getOTPFromUserInput(executorService);
        if ((otpFromUserInput.equals(otp))) {
            System.out.println("Successfully authenticated !");
            final var salt = passwordSecurityManager.generateSalt();
            final var user = new User(userName, email, salt, passwordSecurityManager.encrypt(password, salt));
            userManager.createUser(user);
        }

        executorService.shutdown();
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

        System.out.print("""
            Create a secure password following these rules :
            
            Password length : 8-20 characters.
            It contains at least one digit, one upper case letter, one lower case letter,
            one special character [ !@#$%&*()-+=^. ], doesn’t contain any white space.
            
            """);

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

    private boolean isValidUserName(List<User> users, String userName) {

        if (userName == null || userName.isEmpty()) {
            throw new RuntimeException("Error : Username can't be null or empty !");
        }

        if (userName.length() < 4 || userName.length() > 20) {
            System.out.println("The username length must be 4-20 characters. Choose another one !");
            return false;
        }

        for (var user : users) {
            if (userName.equals(user.getUsername())) {
                System.out.println("This username is already reserved. Choose another one !");
                return false;
            }
        }
        return true;
    }

    private boolean isValidEmail(List<User> users, String email) {

        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Error : Email can't be null or empty !");
        }

        final var regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(regexPattern);
        final Matcher matcher = pattern.matcher(email);

        if (! matcher.matches()) {
            System.out.println("E-mail format is invalid");
            return false;
        }

        for (var user : users) {
            if (email.equals(user.getEmail())) {
                System.out.println("E-mail is already specified, choose another");
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
            // Tell the user he timed out
            logger.info("Error : User timed out to enter OTP !");
            System.out.println("\nTimed out to enter code !");
        }

        return "";
    }
}
