/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.manager;

import com.areg.project.QuizConstants;
import org.apache.commons.lang3.StringUtils;
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
public class EncryptionManager {

    private static final Logger logger = LoggerFactory.getLogger(EncryptionManager.class);

    public String encrypt(String password, String salt) {

        if (StringUtils.isBlank(password)) {
            logger.info("Password is null or empty");
            return "";
        }
        if (StringUtils.isBlank(salt)) {
            logger.info("Salt is null or empty");
            return "";
        }

        final byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        return Base64.getEncoder().encodeToString(securePassword);
    }

    public String generateSalt() {
        final var finalValue = new StringBuilder(QuizConstants.PasswordSaltSize);
        for (short i = 0; i < QuizConstants.PasswordSaltSize; ++i) {
            final Random random = createSecureRandom();
            finalValue.append(QuizConstants.AllCharacters.charAt(random.nextInt(QuizConstants.AllCharacters.length())));
        }
        return finalValue.toString();
    }

    public String generateOneTimePassword() {
        final var s = new StringBuilder();
        for (short i = 0; i < QuizConstants.MailOTPLength; ++i) {
            // Generate random digit within 0-9
            s.append(new Random().nextInt(9));
        }
        return s.toString();
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