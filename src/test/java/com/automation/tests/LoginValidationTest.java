package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Scenario 2 - Input Validation (Error Handling).
 * Verifies the app blocks access and shows the proper alert when the user
 * submits empty fields or invalid credentials.
 */
public class LoginValidationTest extends BaseTest {

    @Test(description = "Empty fields are blocked with a validation alert")
    public void emptyFieldsShowValidationAlert() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoaded(), "Login screen should be displayed on start");

        loginPage.loginExpectingError("", "");

        Assert.assertTrue(loginPage.isErrorAlertDisplayed(),
                "An alert should appear when both fields are empty");
        Assert.assertEquals(loginPage.getErrorAlertText(), "Please complete both fields",
                "Empty submission should warn about missing fields");
    }

    @Test(description = "Invalid credentials are rejected with an error alert")
    public void invalidCredentialsShowErrorAlert() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoaded(), "Login screen should be displayed on start");

        loginPage.loginExpectingError("wrong@email.com", "wrongpass");

        Assert.assertTrue(loginPage.isErrorAlertDisplayed(),
                "An alert should appear for invalid credentials");
        Assert.assertEquals(loginPage.getErrorAlertText(), "Invalid user or password",
                "Invalid credentials should be rejected with the proper message");
    }
}
