#!/bin/bash
echo "=== SiliconFlowMod 构建脚本 ==="

# 检查必要工具
which java >/dev/null || { echo "需要安装 Java"; exit 1; }
which gradle >/dev/null || { 
    echo "安装 Gradle...";
    wget -q https://services.gradle.org/distributions/gradle-7.5.1-bin.zip
    unzip -q gradle-7.5.1-bin.zip
    export PATH=$PATH:$(pwd)/gradle-7.5.1/bin
}

echo "开始构建..."
gradle build

if [ -f "build/libs/siliconflowmod-1.0.0.jar" ]; then
    echo "✅ 构建成功!"
    echo "模组文件: build/libs/siliconflowmod-1.0.0.jar"
else
    echo "❌ 构建失败"
    exit 1
fi
