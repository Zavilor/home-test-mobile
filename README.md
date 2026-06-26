# Home Test Mobile — Art Gallery (Appium + Java + TestNG)

End-to-end automation of the *Art Gallery* Android app with Appium, Java 17, Maven and
TestNG, using the Page Object Model. Covers the 3 required scenarios plus the registration bonus.

## Requirements

- JDK 17 · Maven 3.9+ · Node.js 18+
- Appium 3.x (server) + `uiautomator2` driver
- Android SDK: `platform-tools` (adb), `emulator`, an AVD (API 34), `build-tools`
- Environment: `JAVA_HOME`, `ANDROID_HOME`, and `platform-tools`/`emulator`/Maven on the `PATH`

Install Appium and the driver:

```bash
npm install -g appium
appium driver install uiautomator2
```

## APK

Download `app-home-test-mobile.apk` from the
[challenge release](https://github.com/automationapptest/home-test-mobile/releases/tag/v1.0.0)
and place it at `app/app-home-test.apk` (or pass a different path with `-Dapp=...`). It is not committed (see `.gitignore`).

## How to run

Mobile automation needs 3 processes: emulator + Appium server + the tests.

```powershell
# Terminal 1 — emulator
emulator -avd appium_test

# Terminal 2 — Appium
appium

# Terminal 3 — tests (check first: adb devices)
mvn clean test
```

Shortcut (starts emulator + Appium if missing and runs the suite):

```powershell
.\run-tests.ps1
```

A single scenario:

```bash
mvn clean test -Dtest=LoginTest
```

## Scenarios

| # | Scenario | Class |
|---|----------|-------|
| 1 | Valid login → catalog | `LoginTest` |
| 2 | Validation (empty fields / invalid credentials) | `LoginValidationTest` |
| 3 | Scroll the catalog to a specific art piece | `CatalogTest` |
| 4 | (Bonus) Full user registration | `RegistrationTest` |

Credentials: `johndoe@email.com` / `123`.

## Structure

```
src/test/java/com/automation/
  config/AppiumConfig.java   capabilities and values (overridable with -D)
  base/BaseTest.java         driver lifecycle (new session per test)
  pages/                     Page Objects (Login, Catalog, Register)
  tests/                     one class per scenario
testng.xml                   suite
```

## Design decisions

- **New session per test** → test independence (each one starts at login).
- **Explicit waits** (`WebDriverWait`), no `Thread.sleep`.
- **Locators by `accessibility id`** (React Native testIDs); resource ids for native dialogs.
- **Externalized config** via system properties (`-D...`) → same code locally and in CI.
- **Automatic screenshots** of failed tests in `target/screenshots/`.

## Configuration

Everything is overridable, e.g.:

```bash
mvn clean test -Dapp=app/app-home-test.apk -DvalidEmail=johndoe@email.com -DvalidPassword=123
```

## Reports

- HTML: `target/surefire-reports/emailable-report.html`
- Failure screenshots: `target/screenshots/`
