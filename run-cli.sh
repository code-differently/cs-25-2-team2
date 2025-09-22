#!/bin/bash

# Restaurant Management System CLI Runner
# This script compiles and runs the Restaurant CLI application

echo "🍽️  Starting Restaurant Management System CLI..."
echo "----------------------------------------"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    echo ""
    echo "📥 To install Java on macOS:"
    echo "   Option 1: Install via Homebrew:"
    echo "     brew install openjdk@21"
    echo "     sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk"
    echo ""
    echo "   Option 2: Download from Oracle or OpenJDK:"
    echo "     https://adoptium.net/temurin/releases/?version=21"
    echo ""
    echo "   Option 3: Install via SDKMAN:"
    echo "     curl -s \"https://get.sdkman.io\" | bash"
    echo "     source ~/.sdkman/bin/sdkman-init.sh"
    echo "     sdk install java 21.0.1-tem"
    echo ""
    echo "Please install Java 17 or later and try again."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "⚠️  Warning: Java version $JAVA_VERSION detected. This application requires Java 17 or later."
    echo "Please upgrade your Java installation."
    exit 1
fi

echo "✅ Java version: $(java -version 2>&1 | head -n 1)"

# Navigate to project directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" &> /dev/null && pwd)"
cd "$SCRIPT_DIR"

echo "📁 Working directory: $(pwd)"
echo "🔨 Compiling application..."

# Build the project using Gradle
if ! ./gradlew compileJava --quiet; then
    echo "❌ Compilation failed. Please check for errors above."
    exit 1
fi

echo "✅ Compilation successful!"
echo "🚀 Starting CLI application..."
echo "----------------------------------------"

# Run the CLI application
./gradlew -q --console=plain runCLI

echo "----------------------------------------"
echo "👋 Restaurant Management System CLI ended"