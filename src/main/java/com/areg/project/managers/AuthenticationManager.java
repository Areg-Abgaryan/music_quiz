/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;
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
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);

    private final UserManager userManager;

    @Autowired
    public AuthenticationManager(UserManager userManager) {
        this.userManager = userManager;
    }

    //  FIXME !! Add password hash reverse from the database

    public void authenticate() {

        System.out.print("""
                \n
                1. Login
                2. Sign Up
                Enter option :\s""");

        final var scanner = new Scanner(System.in);
        String option = scanner.next();

        //  FIXME !! Refactor this
        while (! Objects.equals(option, "1") && ! Objects.equals(option, "2")) {
            System.out.println("Wrong input ! Please, choose one of the options above.");
            option = scanner.next();
        }

        switch (option) {
            case "1" -> login();
            case "2" -> signUp();
        }
    }

    private void login() {
        //  FIXME !! Refactor, make only 1 call here, besides, add another call from db for fetching by id
        final var users = userManager.getAllUsers();
    }
    private void signUp() {

        //  FIXME !! Call only when the user is signing up, not logging in
        final var users = userManager.getAllUsers();

        String userName, email, password;

        while (true) {
            System.out.println("Enter e-mail : ");
            final var scanner = new Scanner(System.in);
            String mail = scanner.next();
            if (! isValidEmail(users, mail)) {
                System.out.println("E-mail is already specified, choose another");
            } else {
                email = mail;
                break;
            }
        }

        while (true) {
            System.out.println("Enter username : ");
            final var scanner = new Scanner(System.in);
            String username = scanner.next();
            if (! isValidUserName(users, username)) {
                System.out.println("Username is already specified, choose another");
            } else {
                userName = username;
                break;
            }
        }

        System.out.print("""
            Create a password following these rules :
            
            Password length : 8-20 characters.
            It contains at least one digit, one upper case letter, one lower case letter,
            one special character [ !@#$%&*()-+=^. ], doesn’t contain any white space.\n\n""");

        while (true) {
            System.out.println("Enter password : ");
            final var scanner = new Scanner(System.in);
            String pass = scanner.next();
            if (isValidPassword(pass)) {
                System.out.println("Invalid password, choose another");
            } else {
                System.out.println("Repeat password : ");
                final var scanner2 = new Scanner(System.in);
                String pass2 = scanner2.next();
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

    //  FIXME !! Add username validation [length, startsWith, special characters]
    private boolean isValidUserName(List<User> users, String userName) {
        return users.stream().noneMatch(user -> userName.equals(user.getUserName()));
    }

    //  FIXME !! Add mail validation [endsWith, startsWith, special characters, etc]
    private boolean isValidEmail(List<User> users, String email) {
        return users.stream().noneMatch(user -> email.equals(user.getEmail()));
    }

    private boolean isValidPassword(String password) {
        final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()-+=^.])(?=\\S+$).{8,20}$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /*
    public boolean checkPassword(String password) {

		// if the password doesn't equal itself in full lowercase, it must have an uppercase char
		boolean hasUppercase = !password.equals(password.toLowerCase());
		// and vice versa
		boolean hasLowercase = !password.equals(password.toUpperCase());

		// check for at least one number
		boolean hasNumber = password.matches(".*\\d.*");

		boolean isLongEnough = password.length() >= validLength;

		// default this to true to pass the check if no special characters are required
		boolean hasSpecialCharacters = true;

		if (checkSpecialCharacters) {
			//Checks at least one char is not alphanumeric
			hasSpecialCharacters = !password.matches("[A-Za-z0-9 ]*");
		}

		return hasUppercase && hasLowercase && isLongEnough && hasSpecialCharacters && hasNumber;
	}
     */

    //  FIXME !! Maybe check String == null case
    private String generateSecurePasswordHash(String password) {

        final int iterations = 1000;
        final char[] chars = password.toCharArray();
        final byte[] salt = generateSalt();
        String generatedPassword = null;

        final var spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
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

        final int paddingLength = (array.length * 2) - hex.length();
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
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException nae) {
            logger.warn("Couldn't create strong secure random generator, reason : {}.", nae.getMessage());
            return new SecureRandom();
        }
    }
}
