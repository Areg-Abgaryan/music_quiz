/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;
import java.util.Random;


@Service
public class PasswordSecurityManager {

    private static final Logger logger = LoggerFactory.getLogger(PasswordSecurityManager.class);
    private static PasswordSecurityManager instance;

    private Cipher cipher;
    private SecretKey key;
    private byte[] iv;

    private PasswordSecurityManager() {
        try {
            //  FIXME !! Delete then
            //Decrypter.start();

            final var factory = SecretKeyFactory.getInstance(QuizConstants.SecretKeyAlgorithm);
            final var spec = new PBEKeySpec(QuizConstants.PBEKeySpecPassword.toCharArray(), generateSalt(), QuizConstants.PBEKeyIterations, QuizConstants.PBEKeyLength);
            key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), QuizConstants.SecretKeySpecAlgorithm);
            cipher = Cipher.getInstance(QuizConstants.CipherTransformation);
            //iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PasswordSecurityManager getInstance() {
        if (instance == null) {
            synchronized (PasswordSecurityManager.class) {
                if (instance == null) {
                    instance = new PasswordSecurityManager();
                }
            }
        }
        return instance;
    }

    public String encrypt(String password) {
        try {
            iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            cipher.init(Cipher.ENCRYPT_MODE, key);
            final byte[] utf8EncryptedData = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(utf8EncryptedData);
        } catch (Exception e) {
            logger.error("Error : Password was not encrypted successfully.");
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encryptedPassword) {
        try {

            //  cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            //  byte[] decryptedData = Base64.getDecoder().decode(encryptedPassword);
            //  byte[] utf8 = cipher.doFinal(decryptedData);
            //  return new String(utf8, StandardCharsets.UTF_8);
            iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            cipher.init(Cipher.DECRYPT_MODE, key);
            //cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            final byte[] utf8 = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(utf8, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Error : Password was not decrypted successfully.");
            e.printStackTrace();
        }
        return null;
    }

    //  FIXME !! Add user salt to db
    private byte[] generateSalt() {
        final var random = createSecureRandom();
        final byte[] salt = new byte[QuizConstants.PasswordSaltSize];
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

    private String generateSecurePasswordHash(String password) {

        final char[] chars = password.toCharArray();
        final byte[] salt = generateSalt();
        final var spec = new PBEKeySpec(chars, salt, QuizConstants.PBEKeyIterations, QuizConstants.PBEKeyLength);
        String generatedPassword = null;

        try {
            final SecretKeyFactory skf = SecretKeyFactory.getInstance(QuizConstants.SecretKeyAlgorithm);
            final byte[] hash = skf.generateSecret(spec).getEncoded();
            generatedPassword = QuizConstants.PBEKeyIterations + ":" + toHex(salt) + ":" + toHex(hash);
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
}