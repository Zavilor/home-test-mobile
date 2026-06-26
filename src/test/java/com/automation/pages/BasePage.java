package com.automation.pages;

import com.automation.config.AppiumConfig;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Common helpers shared by every page object: waits, typing and the
 * UiAutomator "scroll into view" gesture used to browse the catalog.
 */
public abstract class BasePage {

    protected final AndroidDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver, Duration.ofSeconds(AppiumConfig.EXPLICIT_WAIT_SECONDS));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void type(By locator, String text) {
        WebElement field = waitVisible(locator);
        field.click();
        field.clear();
        field.sendKeys(text);
    }

    protected void tap(By locator) {
        waitClickable(locator).click();
    }

    /**
     * Closes the soft keyboard if it is currently shown. Typing into a field
     * opens the keyboard, which can cover buttons placed lower on the screen
     * (e.g. the Login button), so we dismiss it before tapping them.
     */
    protected void hideKeyboardIfPresent() {
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
            }
        } catch (Exception ignored) {
            // No keyboard / not dismissable: safe to ignore.
        }
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scrolls a vertically scrollable list until an element with the given
     * visible text appears, then returns it. Throws if it is never found.
     */
    protected WebElement scrollToText(String visibleText) {
        String uiSelector =
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".scrollIntoView(new UiSelector().text(\"" + visibleText + "\"))";
        return driver.findElement(AppiumBy.androidUIAutomator(uiSelector));
    }
}
