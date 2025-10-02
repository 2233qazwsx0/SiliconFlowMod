#!/bin/bash
echo "Building SiliconFlowMod..."

# Install Gradle if not present
if ! command -v gradle &> /dev/null; then
    echo "Installing Gradle..."
    wget https://services.gradle.org/distributions/gradle-7.5.1-bin.zip
    unzip gradle-7.5.1-bin.zip
    export PATH=$PATH:$(pwd)/gradle-7.5.1/bin
fi

# Build the mod
gradle build

echo "Build complete!"
