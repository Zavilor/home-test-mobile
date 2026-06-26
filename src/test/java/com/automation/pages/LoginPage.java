package com.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * The login screen (first screen shown when the app starts).
 */
public class LoginPage extends BasePage {

    private final By emailField = AppiumBy.accessibilityId("emailField");
    private final By passwordField = AppiumBy.accessibilityId("passwordField");
    private final By loginButton = AppiumBy.accessibilityId("Login");
    private final By registerButton = AppiumBy.accessibilityId("registerButton");

    // Native AlertDialog shown for validation / invalid credentials.
    private final By alertTitle = By.id("com.learnautomationapp:id/alertTitle");
    private final By alertOkButton = By.id("android:id/button1");

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(emailField) && isDisplayed(passwordField);
    }

    public LoginPage enterEmail(String email) {
        type(emailField, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public void tapLogin() {
        // The soft keyboard opened while typing can cover the Login button.
        hideKeyboardIfPresent();
        tap(loginButton);
    }

    /** Full happy-path login that lands on the catalog. */
    public CatalogPage loginExpectingSuccess(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        tapLogin();
        return new CatalogPage(driver);
    }

    /** Login attempt expected to fail and raise a native alert. */
    public LoginPage loginExpectingError(String email, String password) {
        if (email != null && !email.isEmpty()) {
            enterEmail(email);
        }
        if (password != null && !password.isEmpty()) {
            enterPassword(password);
        }
        tapLogin();
        return this;
    }

    public boolean isErrorAlertDisplayed() {
        return isDisplayed(alertTitle);
    }

    public String getErrorAlertText() {
        return waitVisible(alertTitle).getText();
    }

    public LoginPage dismissAlert() {
        tap(alertOkButton);
        return this;
    }

    public RegisterPage goToRegister() {
        tap(registerButton);
        return new RegisterPage(driver);
    }
}
