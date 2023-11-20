/**
 * Copyright (c) 2023 Areg Abgaryan
 */

import com.areg.project.QuizConstants;
import com.areg.project.managers.PasswordSecurityManager;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PasswordSecurityManagerTest {

    private PasswordSecurityManager passwordSecurityManager;

    @BeforeTest
    void setUp() {
        passwordSecurityManager = new PasswordSecurityManager();
    }

    @Test
    void testEncryptWhenPasswordOrSaltIsEmpty() {
        String password = "";
        String salt = passwordSecurityManager.generateSalt();
        String encryptedPassword = passwordSecurityManager.encrypt(password, salt);
        Assert.assertTrue(encryptedPassword.isEmpty());

        password = "newPassword123";
        salt = "";
        encryptedPassword = passwordSecurityManager.encrypt(password, salt);
        Assert.assertTrue(encryptedPassword.isEmpty());
    }

    @Test
    void testEncrypt() {
        final String password = "testPassword";
        final String salt = passwordSecurityManager.generateSalt();
        final String encryptedPassword = passwordSecurityManager.encrypt(password, salt);
        Assert.assertFalse(StringUtils.isBlank(encryptedPassword));
    }

    @Test
    void testGenerateSalt() {
        final String salt = passwordSecurityManager.generateSalt();
        Assert.assertNotNull(salt);
        Assert.assertEquals(salt.length(), QuizConstants.PasswordSaltSize);
    }

    @Test
    void testGenerateOneTimePassword() {
        final String otp = passwordSecurityManager.generateOneTimePassword();
        Assert.assertNotNull(otp);
        Assert.assertEquals(otp.length(), QuizConstants.MailOTPLength);
    }
}
