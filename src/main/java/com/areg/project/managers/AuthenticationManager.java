/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//  FIXME !! Consider adding user info to quiz mode context after authentication
@Service
public class AuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);
    private final UserManager userManager;
    private final PasswordSecurityManager passwordSecurityManager = PasswordSecurityManager.getInstance();

    @Autowired
    public AuthenticationManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void authenticate() {
        //  FIXME !! Remove this after db fix
        /*
        final var chel = new Artist("Cardie B");
        final var artistManager = new ArtistManager();
        artistManager.createArtist(chel);

        var getingChel = artistManager.getArtist(1L);
        var albumFirst = new Album("album1", getingChel, (short) 2012, (byte) 13, "43:57", null, QuizDifficulty.EASY);
        var albumSecond = new Album("album2", getingChel, (short) 2015, (byte) 10, "41:29", null, QuizDifficulty.EASY);

        var albumManager = new AlbumManager();
        albumManager.createAlbum(albumFirst, getingChel);
        albumManager.createAlbum(albumSecond, getingChel);

        final var songManager = new SongManager();
        final var songFirstAlbumFirst = new Song("Song1", getingChel, albumFirst, "3:05", (byte) 1);
        final var songFirstAlbumSecond = new Song("Song2", getingChel, albumFirst, "3:16", (byte) 1);
        final var songSecondAlbumFirst = new Song("Song3", getingChel, albumSecond, "3:27", (byte) 1);
        final var songSecondAlbumSecond = new Song("Song4", getingChel, albumSecond, "3:38", (byte) 1);

        songManager.createSong(songFirstAlbumFirst,albumFirst,getingChel);
        songManager.createSong(songFirstAlbumSecond,albumFirst,getingChel);
        songManager.createSong(songSecondAlbumFirst,albumSecond,getingChel);
        songManager.createSong(songSecondAlbumSecond,albumSecond,getingChel);

         */

        //  O7J7LxIdslOLglJEhvDNxQ==
        //  works when encrypted / decrypted, doesn't work when decrypted
        var e = passwordSecurityManager.encrypt("Aregushka15");
        var d = passwordSecurityManager.decrypt("0KTsZCCn1NCx4+xmPZJexw==");


        System.out.print("""
                \n
                1. Log In
                2. Sign Up
                Enter option :\s""");

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
                //  FIXME !! Decrypt password here, the condition is always false
                if (! password.equals(passwordSecurityManager.decrypt(user.getPassword()))) {
                    System.out.println("Invalid password provided !");
                } else {
                    logger.info("User {} successfully logged in !", user.getUserName());
                    System.out.println("Successfully logged in !");
                    break;
                }
            } else {
                System.out.println("Invalid email provided !");
            }
        }
    }

    private void signUp() {

        final var users = userManager.getAllUsers();
        String userName, email, password;

        while (true) {
            System.out.println("Enter e-mail : ");
            final var scanner = new Scanner(System.in);
            final String mail = scanner.next();
            if (isValidEmail(users, mail)) {
                email = mail;
                break;
            }
        }

        while (true) {
            System.out.println("Enter username : ");
            final var scanner = new Scanner(System.in);
            final String username = scanner.next();
            if (isValidUserName(users, username)) {
                userName = username;
                break;
            }
        }

        System.out.print("""
            Create a secure password following these rules :
            
            Password length : 8-20 characters.
            It contains at least one digit, one upper case letter, one lower case letter,
            one special character [ !@#$%&*()-+=^. ], doesn’t contain any white space.
            
            """);

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

        final var user = new User(userName, email, passwordSecurityManager.encrypt(password));
        userManager.createUser(user);
    }

    private boolean isValidUserName(List<User> users, String userName) {

        //  FIXME !! Consider changing this to logger.error
        if (userName == null || userName.isEmpty()) {
            throw new RuntimeException("Error : Username can't be null or empty !");
        }

        if (userName.length() < 4 || userName.length() > 20) {
            System.out.println("The username length must be 4-20 characters. Choose another one !");
            return false;
        }

        for (var user : users) {
            if (userName.equals(user.getUserName())) {
                System.out.println("This username is already reserved. Choose another one !");
                return false;
            }
        }
        return true;
    }

    private boolean isValidEmail(List<User> users, String email) {

        //  FIXME !! Consider changing this to logger.error
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

        //  FIXME !! Consider changing this to logger.error
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
}
