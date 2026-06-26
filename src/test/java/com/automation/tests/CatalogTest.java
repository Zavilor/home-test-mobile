package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.config.AppiumConfig;
import com.automation.pages.CatalogPage;
import com.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Scenario 3 - Catalog Exploration (Native Interaction).
 * Logs in, scrolls the catalog to a specific art piece deep in the list
 * and verifies its presence.
 */
public class CatalogTest extends BaseTest {

    private static final String TARGET_ART_PIECE = "Twilight Glow";

    @Test(description = "Scroll the catalog to locate a deep art piece")
    public void scrollToFindArtPiece() {
        CatalogPage catalogPage = new LoginPage(driver)
                .loginExpectingSuccess(AppiumConfig.VALID_EMAIL, AppiumConfig.VALID_PASSWORD);

        Assert.assertTrue(catalogPage.isLoaded(), "Catalog should be displayed after login");

        Assert.assertTrue(catalogPage.isArtPieceVisible(TARGET_ART_PIECE),
                "Art piece '" + TARGET_ART_PIECE + "' should be found after scrolling the catalog");
    }
}
