package com.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Registration flow. It spans two steps (account + personal information),
 * a native date picker, a terms checkbox and a final success screen.
 */
public class RegisterPage extends BasePage {

    // Step 1 - account data
    private final By emailField = AppiumBy.accessibilityId("emailField");
    private final By firstNameField = AppiumBy.accessibilityId("firstNameField");
    private final By lastNameField = AppiumBy.accessibilityId("lastNameField");
    private final By passwordField = AppiumBy.accessibilityId("passwordField");
    private final By continueButton = AppiumBy.accessibilityId("Continue");

    // Step 2 - personal information
    private final By addressInput = AppiumBy.accessibilityId("addressInput");
    private final By cityInput = AppiumBy.accessibilityId("cityInput");
    private final By zipInput = AppiumBy.accessibilityId("zipInput");
    private final By openDatePicker = AppiumBy.accessibilityId("openDatePicker");
    private final By termsCheckbox = AppiumBy.accessibilityId("termConditions");
    private final By signupButton = AppiumBy.accessibilityId("Signup!");

    // Native date picker buttons
    private final By datePickerConfirm = By.id("android:id/button1");

    // Success screen
    private final By successTitle = AppiumBy.accessibilityId("title");
    private final By goToLoginButton = AppiumBy.accessibilityId("Go to Login");

    public RegisterPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        // firstNameField only exists on the Register screen (the login screen
        // also has an emailField), so it is the reliable "are we here" marker
        // once the navigation animation settles.
        return isDisplayed(firstNameField);
    }

    public RegisterPage fillAccountStep(String email, String firstName,
                                        String lastName, String password) {
        type(emailField, email);
        type(firstNameField, firstName);
        type(lastNameField, lastName);
        type(passwordField, password);
        return this;
    }

    public RegisterPage tapContinue() {
        hideKeyboardIfPresent();
        tap(continueButton);
        return this;
    }

    public RegisterPage fillPersonalInfoStep(String address, String city, String zip) {
        type(addressInput, address);
        type(cityInput, city);
        type(zipInput, zip);
        return this;
    }

    /** Opens the native date picker and confirms the default date. */
    public RegisterPage selectBirthDate() {
        hideKeyboardIfPresent();
        tap(openDatePicker);
        tap(datePickerConfirm);
        return this;
    }

    public RegisterPage acceptTerms() {
        tap(termsCheckbox);
        return this;
    }

    public RegisterPage submit() {
        hideKeyboardIfPresent();
        tap(signupButton);
        return this;
    }

    public boolean isRegistrationSuccessful() {
        return isDisplayed(goToLoginButton) || isDisplayed(successTitle);
    }

    public String getSuccessTitle() {
        return waitVisible(successTitle).getText();
    }
}
