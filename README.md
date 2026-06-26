# Home Test Mobile — Art Gallery (Appium + Java + TestNG)

Automatización end-to-end de la app Android *Art Gallery* con Appium, Java 17, Maven y
TestNG, usando Page Object Model. Cubre los 3 escenarios pedidos más el bonus de registro.

## Requisitos

- JDK 17 · Maven 3.9+ · Node.js 18+
- Appium 3.x (server) + driver `uiautomator2`
- Android SDK: `platform-tools` (adb), `emulator`, un AVD (API 34), `build-tools`
- Variables: `JAVA_HOME`, `ANDROID_HOME`, y `platform-tools`/`emulator`/Maven en el `PATH`

Instalar Appium y el driver:

```bash
npm install -g appium
appium driver install uiautomator2
```

## APK

Descargá `app-home-test-mobile.apk` de la
[release del challenge](https://github.com/automationapptest/home-test-mobile/releases/tag/v1.0.0)
y dejalo en `app/app-home-test.apk` (o pasá otra ruta con `-Dapp=...`). No se commitea (ver `.gitignore`).

## Cómo correr

La automatización mobile necesita 3 procesos: emulador + servidor Appium + los tests.

```powershell
# Terminal 1 — emulador
emulator -avd appium_test

# Terminal 2 — Appium
appium

# Terminal 3 — tests (verificar antes: adb devices)
mvn clean test
```

Atajo (levanta emulador + Appium si faltan y corre la suite):

```powershell
.\run-tests.ps1
```

Un solo escenario:

```bash
mvn clean test -Dtest=LoginTest
```

## Escenarios

| # | Escenario | Clase |
|---|-----------|-------|
| 1 | Login válido → catálogo | `LoginTest` |
| 2 | Validación (campos vacíos / credenciales inválidas) | `LoginValidationTest` |
| 3 | Scroll del catálogo hasta una obra puntual | `CatalogTest` |
| 4 | (Bonus) Registro completo de usuario | `RegistrationTest` |

Credenciales: `johndoe@email.com` / `123`.

## Estructura

```
src/test/java/com/automation/
  config/AppiumConfig.java   capabilities y valores (overrideables con -D)
  base/BaseTest.java         ciclo de vida del driver (sesión nueva por test)
  pages/                     Page Objects (Login, Catalog, Register)
  tests/                     una clase por escenario
testng.xml                   suite
```

## Decisiones de diseño

- **Sesión nueva por test** → independencia entre tests (cada uno arranca en login).
- **Esperas explícitas** (`WebDriverWait`), sin `Thread.sleep`.
- **Locators por `accessibility id`** (testIDs de React Native); resource ids para diálogos nativos.
- **Config externalizada** con system properties (`-D...`) → mismo código local y CI.
- **Capturas automáticas** de tests fallidos en `target/screenshots/`.

## Configuración

Todo es overrideable, p.ej.:

```bash
mvn clean test -Dapp=app/app-home-test.apk -DvalidEmail=johndoe@email.com -DvalidPassword=123
```

## Reportes

- HTML: `target/surefire-reports/emailable-report.html`
- Capturas de fallos: `target/screenshots/`
