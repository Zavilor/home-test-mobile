package com.automation.config;

/**
 * Central place for environment / capability values.
 * Everything can be overridden with -D system properties so the same code
 * runs locally and on CI without edits.
 */
public final class AppiumConfig {

    private AppiumConfig() {
    }

    public static final String APPIUM_SERVER_URL =
            System.getProperty("appium.url", "http://127.0.0.1:4723");

    public static final String PLATFORM_NAME =
            System.getProperty("platformName", "Android");

    public static final String AUTOMATION_NAME =
            System.getProperty("automationName", "UiAutomator2");

    public static final String DEVICE_NAME =
            System.getProperty("deviceName", "Android Emulator");

    public static final String APP_PACKAGE =
            System.getProperty("appPackage", "com.learnautomationapp");

    public static final String APP_ACTIVITY =
            System.getProperty("appActivity", "com.learnautomationapp.MainActivity");

    /**
     * Absolute or relative path to the .apk under test.
     * Relative paths are resolved against the project root.
     */
    public static final String APP_PATH =
            System.getProperty("app", "app/app-home-test.apk");

    public static final int IMPLICIT_WAIT_SECONDS =
            Integer.getInteger("implicitWait", 5);

    public static final int EXPLICIT_WAIT_SECONDS =
            Integer.getInteger("explicitWait", 20);

    // Valid demo credentials provided by the challenge.
    public static final String VALID_EMAIL =
            System.getProperty("validEmail", "johndoe@email.com");

    public static final String VALID_PASSWORD =
            System.getProperty("validPassword", "123");
}
