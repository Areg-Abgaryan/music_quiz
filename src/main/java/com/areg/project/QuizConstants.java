/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

public class QuizConstants {
    public static final short NumberOfRounds = 10;
    public static final byte RoundOptions = 4;
    public static final byte RoundTimeoutSeconds = 20;
    public static final byte PasswordSaltSize = 4;
    public static final String SecretKeyAlgorithm = "PBKDF2WithHmacSHA1";
    public static final String RNGAlgorithm = "SHA1PRNG";
    public static final short PBEKeyIterations = 1000;
    public static final short PBEKeyLength = 256;
}
