<#
.SYNOPSIS
    Levanta todo el entorno y corre la suite de tests con un solo comando.

.DESCRIPTION
    Hace, en orden, lo que normalmente harias a mano en 3 terminales:
      1. Arranca el emulador Android (si no hay ninguno conectado) y espera el boot.
      2. Arranca el servidor Appium (si el puerto 4723 esta libre).
      3. Ejecuta 'mvn clean test'.

    Es idempotente: si el emulador o Appium ya estan corriendo, los reutiliza.

.PARAMETER Avd
    Nombre del AVD a usar. Por defecto 'appium_test'.

.PARAMETER Test
    (Opcional) Corre una sola clase de test, ej: -Test LoginTest

.EXAMPLE
    .\run-tests.ps1
    .\run-tests.ps1 -Test RegistrationTest
#>

param(
    [string]$Avd  = "appium_test",
    [string]$Test = ""
)

$ErrorActionPreference = "Stop"

# --- Rutas a las herramientas del Android SDK ---
$sdk      = if ($env:ANDROID_HOME) { $env:ANDROID_HOME } else { "$env:LOCALAPPDATA\Android\Sdk" }
$adb      = Join-Path $sdk "platform-tools\adb.exe"
$emulator = Join-Path $sdk "emulator\emulator.exe"

function Write-Step($msg) { Write-Host "`n==> $msg" -ForegroundColor Cyan }

# --- 1. Emulador -------------------------------------------------------------
Write-Step "Verificando emulador..."
$devices = & $adb devices | Select-String "emulator-\d+\s+device"
if ($devices) {
    Write-Host "    Ya hay un emulador conectado. Lo reutilizo." -ForegroundColor Green
} else {
    Write-Host "    No hay emulador. Arrancando '$Avd'..." -ForegroundColor Yellow
    Start-Process -FilePath $emulator -ArgumentList "-avd", $Avd, "-no-snapshot-load" | Out-Null

    Write-Host "    Esperando el boot del sistema (puede tardar ~1 min)..."
    & $adb wait-for-device
    for ($i = 0; $i -lt 60; $i++) {
        $booted = (& $adb shell getprop sys.boot_completed 2>$null) -match "1"
        if ($booted) { break }
        Start-Sleep -Seconds 3
    }
    if (-not $booted) { throw "El emulador no termino de bootear a tiempo." }
    Write-Host "    Emulador listo." -ForegroundColor Green
}

# --- 2. Servidor Appium ------------------------------------------------------
Write-Step "Verificando servidor Appium (puerto 4723)..."
$appiumUp = Get-NetTCPConnection -LocalPort 4723 -State Listen -ErrorAction SilentlyContinue
if ($appiumUp) {
    Write-Host "    Appium ya esta escuchando. Lo reutilizo." -ForegroundColor Green
} else {
    Write-Host "    Arrancando Appium en una ventana aparte..." -ForegroundColor Yellow
    Start-Process -FilePath "appium" -ArgumentList "--address", "127.0.0.1", "--port", "4723"
    # Esperamos a que el puerto quede escuchando.
    for ($i = 0; $i -lt 30; $i++) {
        if (Get-NetTCPConnection -LocalPort 4723 -State Listen -ErrorAction SilentlyContinue) { break }
        Start-Sleep -Seconds 1
    }
    Write-Host "    Appium listo." -ForegroundColor Green
}

# --- 3. Tests ----------------------------------------------------------------
Write-Step "Corriendo los tests..."
if ($Test) {
    mvn clean test "-Dtest=$Test"
} else {
    mvn clean test
}
