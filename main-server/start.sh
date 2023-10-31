#!/bin/bash

# 项目的根目录
PROJECT_DIR="/root/server/server/main-server"

# Spring Boot应用的主类
MAIN_CLASS="com.pj.SpServerApplication"  # 请替换为你的主类

# 启动Spring Boot项目
nohup java -cp "$PROJECT_DIR" "$MAIN_CLASS" > "$PROJECT_DIR/application.log" 2>&1 &

# 打印启动信息
echo "Starting Spring Boot application in the background..."
