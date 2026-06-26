package com.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * The Art Gallery catalog shown after a successful login.
 */
public class CatalogPage extends BasePage {

    private final By title = AppiumBy.accessibilityId("title");
    private final By itemsList = AppiumBy.accessibilityId("itemsList");

    public CatalogPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(title) && isDisplayed(itemsList);
    }

    public String getTitle() {
        return waitVisible(title).getText();
    }

    /**
     * Scrolls down the catalog until the art piece with the given name is
     * visible and returns its element.
     */
    public WebElement findArtPiece(String name) {
        return scrollToText(name);
    }

    public boolean isArtPieceVisible(String name) {
        try {
            return findArtPiece(name).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectArtPiece(String name) {
        findArtPiece(name).click();
    }
}
