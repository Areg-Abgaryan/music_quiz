/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project;

import java.time.ZoneId;

public class QuizConstants {

    public static final short NumberOfRounds = 10;
    public static final byte RoundOptions = 4;
    public static final byte RoundTimeoutSeconds = 20;
    public static final byte PasswordSaltSize = 16;
    public static final String SecretKeyAlgorithm = "PBKDF2WithHmacSHA1";
    public static final String RNGAlgorithm = "SHA1PRNG";
    public static final short PBEKeyIterations = 1024;
    public static final short PBEKeyLength = 256;
    public static final String AllCharacters;
    public static final String MailServerAddress = "";
    public static final short MailOTPLength = 6;
    public static final byte OTPTimeoutSeconds = 60;
    public static ZoneId TimeZoneId = ZoneId.of("Asia/Yerevan");

    static {
        AllCharacters = generateAllCharacters();
    }

    private static String generateAllCharacters() {
        final var allCharacters = new StringBuilder();
        appendRange(allCharacters, '!', '-');
        appendRange(allCharacters, '0', '9');
        appendRange(allCharacters, 'A', 'Z');
        appendRange(allCharacters, 'a', 'z');
        return allCharacters.toString();
    }

    private static void appendRange(StringBuilder builder, char start, char end) {
        for (char c = start; c <= end; ++c) {
            builder.append(c);
        }
    }
}
