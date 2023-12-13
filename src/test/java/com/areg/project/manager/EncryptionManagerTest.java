/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.manager;

import com.areg.project.QuizConstants;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class EncryptionManagerTest {

    private EncryptionManager encryptionManager;

    @BeforeTest
    void setUp() {
        encryptionManager = new EncryptionManager();
    }

    @Test
    void testEncryptWhenPasswordOrSaltIsEmpty() {
        String password = "";
        String salt = encryptionManager.generateSalt();
        String encryptedPassword = encryptionManager.encrypt(password, salt);
        Assert.assertTrue(encryptedPassword.isEmpty());

        password = "newPassword123";
        salt = "";
        encryptedPassword = encryptionManager.encrypt(password, salt);
        Assert.assertTrue(encryptedPassword.isEmpty());
    }

    @Test
    void testEncrypt() {
        final String password = "testPassword";
        final String salt = encryptionManager.generateSalt();
        final String encryptedPassword = encryptionManager.encrypt(password, salt);
        Assert.assertFalse(StringUtils.isBlank(encryptedPassword));
    }

    @Test
    void testGenerateSalt() {
        final String salt = encryptionManager.generateSalt();
        Assert.assertNotNull(salt);
        Assert.assertEquals(salt.length(), QuizConstants.PasswordSaltSize);
    }

    @Test
    void testGenerateOneTimePassword() {
        final String otp = encryptionManager.generateOneTimePassword();
        Assert.assertNotNull(otp);
        Assert.assertEquals(otp.length(), QuizConstants.MailOTPLength);
    }
}
