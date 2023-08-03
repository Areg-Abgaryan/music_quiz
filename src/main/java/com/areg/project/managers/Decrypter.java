/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.QuizConstants;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

//  FIXME !! Delete then
public class Decrypter {

    private final Cipher dcipher;
    private final byte[] salt = generateSalt();
    private final int iterationCount = 1024;
    private final int keyStrength = 256;
    private final SecretKey key;
    private byte[] iv;

    Decrypter(String passPhrase) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount, keyStrength);
        SecretKey tmp = factory.generateSecret(spec);
        key = new SecretKeySpec(tmp.getEncoded(), "AES");
        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    public String encrypt(String data) throws Exception {
        dcipher.init(Cipher.ENCRYPT_MODE, key);
        final var params = dcipher.getParameters();
        iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] utf8EncryptedData = dcipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(utf8EncryptedData);
    }

    public String decrypt(String base64EncryptedData) throws Exception {
        dcipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        final byte[] decryptedData = Base64.getDecoder().decode(base64EncryptedData);
        final byte[] utf8 = dcipher.doFinal(decryptedData);
        return new String(utf8, StandardCharsets.UTF_8);
    }

    public static void start() throws Exception {
        final Decrypter decrypter = new Decrypter("ABCDEFGHIJKL");
        final String encrypted = decrypter.encrypt("the  1234 quick brown fox jumps over the lazy dog");
        final String decrypted = decrypter.decrypt(encrypted);
        System.out.println(decrypted);
    }

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
            return new SecureRandom();
        }
    }
}