/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

@Service
public class PasswordSecurityManager {

    private static final Logger logger = LoggerFactory.getLogger(PasswordSecurityManager.class);

    public String encrypt(String password, String salt) {
        final byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        return Base64.getEncoder().encodeToString(securePassword);
    }

    public String generateSalt(int length) {
        final var finalValue = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            final Random random = createSecureRandom();
            finalValue.append(QuizConstants.AllCharacters.charAt(random.nextInt(QuizConstants.AllCharacters.length())));
        }
        return new String(finalValue);
    }

    private byte[] hash(char[] password, byte[] salt) {
        final var spec = new PBEKeySpec(password, salt, QuizConstants.PBEKeyIterations, QuizConstants.PBEKeyLength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            final var skf = SecretKeyFactory.getInstance(QuizConstants.SecretKeyAlgorithm);
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
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