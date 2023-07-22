/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;
import com.areg.project.QuizDifficulty;
import com.areg.project.entities.Album;
import com.areg.project.entities.Artist;
import com.areg.project.entities.Song;
import com.areg.project.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);

    private final UserManager userManager;

    private final ArtistManager artistManager;

    @Autowired
    public AuthenticationManager(UserManager userManager, ArtistManager artistManager) {
        this.userManager = userManager;
        this.artistManager = artistManager;
    }

    public void authenticate() {

        /*  //  FIXME !! Remove this after db fix
        //  Test data
        var chel = new Artist("Cardie B");
        artistManager.createArtist(chel);

        var albumFirst = new Album("album1", chel, (short) 2012, (byte) 13, "43:57", null, QuizDifficulty.EASY);
        var albumSecond = new Album("album2", chel, (short) 2015, (byte) 10, "41:29", null, QuizDifficulty.EASY);

        var albumManager = new AlbumManager();
        albumManager.createAlbum(albumFirst, chel);
        albumManager.createAlbum(albumSecond, chel);

        final var songManager = new SongManager();
        final var songFirstAlbumFirst = new Song("Song1", chel, albumFirst, "3:05", (byte) 1);
        final var songFirstAlbumSecond = new Song("Song2", chel, albumFirst, "3:16", (byte) 1);
        final var songSecondAlbumFirst = new Song("Song3", chel, albumSecond, "3:27", (byte) 1);
        final var songSecondAlbumSecond = new Song("Song4", chel, albumSecond, "3:38", (byte) 1);

        songManager.createSong(songFirstAlbumFirst,albumFirst,chel);
        songManager.createSong(songFirstAlbumSecond,albumFirst,chel);
        songManager.createSong(songSecondAlbumFirst,albumSecond,chel);
        songManager.createSong(songSecondAlbumSecond,albumSecond,chel);

        */

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
                if (! password.equals(user.getPassword())) {
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
            Create a password following these rules :
            
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

        final var user = new User(userName, email, generateSecurePasswordHash(password));
        userManager.createUser(user);
    }

    private boolean isValidUserName(List<User> users, String userName) {
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
        final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()-+=^.])(?=\\S+$).{8,20}$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(password);
        if (! matcher.matches()) {
            System.out.println("Invalid password, choose another");
            return false;
        }
        return true;
    }

    private String generateSecurePasswordHash(String password) {

        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Error : Password can't be null or empty !");
        }

        final short iterations = 1000;
        final char[] chars = password.toCharArray();
        final byte[] salt = generateSalt();
        String generatedPassword = null;

        final var spec = new PBEKeySpec(chars, salt, iterations, 32 * 8);
        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance(QuizConstants.SecretKeyAlgorithm);
            final byte[] hash = skf.generateSecret(spec).getEncoded();
            generatedPassword = iterations + ":" + toHex(salt) + ":" + toHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }

    private String toHex(byte[] array) {
        final var bi = new BigInteger(1, array);
        final String hex = bi.toString(16);

        final short paddingLength = (short) ((array.length * 2) - hex.length());
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private byte[] generateSalt() {
        final var random = createSecureRandom();
        byte[] salt = new byte[QuizConstants.PasswordSaltSize];
        random.nextBytes(salt);
        return salt;
    }

    private Random createSecureRandom() {
        try {
            return SecureRandom.getInstance(QuizConstants.RNGAlgorithm);
        } catch (NoSuchAlgorithmException nae) {
            logger.info("Couldn't create strong secure random generator, reason : {}.", nae.getMessage());
            return new SecureRandom();
        }
    }
}
