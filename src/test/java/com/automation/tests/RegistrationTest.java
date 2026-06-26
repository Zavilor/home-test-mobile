package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import com.automation.pages.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Scenario 4 (Bonus) - User Registration Flow.
 * Navigates from login to the registration screen, completes the sign-up
 * form (including the date picker selection component and the terms
 * checkbox) and verifies the registration success screen is reached.
 */
public class RegistrationTest extends BaseTest {

    @Test(description = "Complete the registration flow up to the success screen")
    public void registerNewUser() {
        long unique = System.currentTimeMillis();

        RegisterPage registerPage = new LoginPage(driver).goToRegister();
        Assert.assertTrue(registerPage.isLoaded(), "Registration screen should be displayed");

        registerPage
                .fillAccountStep("user" + unique + "@email.com", "Juan", "Rettori", "123456")
                .tapContinue()
                .fillPersonalInfoStep("Arenales 2534", "Florida", "12345")
                .selectBirthDate()
                .acceptTerms()
                .submit();

        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "Registration success screen should be reached");
        Assert.assertEquals(registerPage.getSuccessTitle(), "Your user has been created.",
                "Success screen should confirm the account was created");
    }
}
