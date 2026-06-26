package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.config.AppiumConfig;
import com.automation.pages.CatalogPage;
import com.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Scenario 1 - User Authentication (Happy Path).
 * Logs in with valid credentials and asserts the app transitions to the
 * Art Gallery catalog view.
 */
public class LoginTest extends BaseTest {

    @Test(description = "Valid login transitions the user to the catalog view")
    public void validLoginShowsCatalog() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoaded(), "Login screen should be displayed on start");

        CatalogPage catalogPage = loginPage.loginExpectingSuccess(
                AppiumConfig.VALID_EMAIL, AppiumConfig.VALID_PASSWORD);

        Assert.assertTrue(catalogPage.isLoaded(),
                "Catalog (Art Gallery) view should be displayed after a successful login");
        Assert.assertEquals(catalogPage.getTitle(), "Inventory",
                "Catalog title should confirm we reached the gallery view");
    }
}
