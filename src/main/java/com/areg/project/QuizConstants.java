/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

public class QuizConstants {
    public static final short NumberOfRounds = 10;
    public static final byte RoundOptions = 4;
    public static final byte RoundTimeoutSeconds = 20;
    public static final byte PasswordSaltSize = 8;
    public static final String SecretKeyAlgorithm = "PBKDF2WithHmacSHA1";
    public static final String RNGAlgorithm = "SHA1PRNG";
    public static final String SecretKeySpecAlgorithm = "AES";
    public static final String CipherTransformation = "AES/CBC/PKCS5Padding";
    public static final String PBEKeySpecPassword = "decrypter_password";
    public static final short PBEKeyIterations = 1024;
    public static final short PBEKeyLength = 256;
}
