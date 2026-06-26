package com.automation.base;

import com.automation.config.AppiumConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

/**
 * Creates a fresh AndroidDriver before every test method and disposes it
 * afterwards. A new session per test guarantees the app starts from a clean
 * state (login screen), so tests stay independent.
 */
public abstract class BaseTest {

    protected AndroidDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(AppiumConfig.PLATFORM_NAME)
                .setAutomationName(AppiumConfig.AUTOMATION_NAME)
                .setDeviceName(AppiumConfig.DEVICE_NAME)
                .setAppPackage(AppiumConfig.APP_PACKAGE)
                .setAppActivity(AppiumConfig.APP_ACTIVITY)
                .setApp(resolveAppPath())
                // Clear app data on session start -> always land on the login screen.
                .setNoReset(false)
                .setFullReset(false)
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(120));

        driver = new AndroidDriver(new URL(AppiumConfig.APPIUM_SERVER_URL), options);
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(AppiumConfig.IMPLICIT_WAIT_SECONDS));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (driver != null && result.getStatus() == ITestResult.FAILURE) {
                captureScreenshot(result.getName());
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private String resolveAppPath() {
        File app = new File(AppiumConfig.APP_PATH);
        if (!app.isAbsolute()) {
            app = new File(System.getProperty("user.dir"), AppiumConfig.APP_PATH);
        }
        if (!app.exists()) {
            throw new IllegalStateException(
                    "APK not found at: " + app.getAbsolutePath()
                            + ". Download it into the 'app' folder (see README).");
        }
        return app.getAbsolutePath();
    }

    private void captureScreenshot(String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path dir = Path.of(System.getProperty("user.dir"), "target", "screenshots");
            Files.createDirectories(dir);
            Files.copy(src.toPath(),
                    dir.resolve(testName + "_" + System.currentTimeMillis() + ".png"));
        } catch (Exception ignored) {
            // Screenshot is best-effort; never fail teardown because of it.
        }
    }
}
