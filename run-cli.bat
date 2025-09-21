@echo off
REM Restaurant Management System CLI Runner for Windows
REM This script compiles and runs the Restaurant CLI application

echo 🍽️  Starting Restaurant Management System CLI...
echo ----------------------------------------

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed or not in PATH
    echo.
    echo 📥 To install Java on Windows:
    echo    Option 1: Download from Oracle or OpenJDK:
    echo      https://adoptium.net/temurin/releases/?version=21
    echo.
    echo    Option 2: Install via Chocolatey:
    echo      choco install openjdk21
    echo.
    echo    Option 3: Install via Scoop:
    echo      scoop bucket add java
    echo      scoop install openjdk21
    echo.
    echo Please install Java 17 or later and try again.
    pause
    exit /b 1
)

echo ✅ Java is installed
java -version

REM Navigate to script directory
cd /d "%~dp0"

echo 📁 Working directory: %cd%
echo 🔨 Compiling application...

REM Build the project using Gradle
gradlew.bat compileJava --quiet
if %errorlevel% neq 0 (
    echo ❌ Compilation failed. Please check for errors above.
    pause
    exit /b 1
)

echo ✅ Compilation successful!
echo 🚀 Starting CLI application...
echo ----------------------------------------

REM Run the CLI application
gradlew.bat -q --console=plain runCLI

echo ----------------------------------------
echo 👋 Restaurant Management System CLI ended
pause