@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
cd /d %~dp0

:: 设置是否显示控制台窗口 (1=显示，0=隐藏)
set "SHOW_CONSOLE=0"

:: 设置根目录
set "ROOT_DIR=%~dp0"
echo 项目根目录: %ROOT_DIR%

echo 正在检测环境版本...
echo.

:: 1. JDK版本检测
where java >nul 2>nul
if %errorlevel% equ 0 (
    echo [JDK 版本]
    for /f "tokens=3" %%a in ('java -version 2^>^&1 ^| findstr /i "version"') do (
        set "jdk_ver=%%a"
    )
    set "jdk_ver=!jdk_ver:"=!"
    echo 当前版本: !jdk_ver!
    for /f "tokens=1,2 delims=._" %%a in ("!jdk_ver!") do (
        if %%a geq 17 (echo ✅ JDK版本符合要求（≥17）) else (echo ❌ JDK版本过低，需要≥17)
    )
) else (
    echo ❌ 未检测到JDK
)
echo.

:: 2. Node.js版本检测
where node >nul 2>nul
if %errorlevel% equ 0 (
    echo [Node.js 版本]
    for /f "delims=" %%a in ('node -v 2^>nul') do set "node_ver=%%a"
    set "node_ver=!node_ver:~1!"
    echo 当前版本: !node_ver!
    for /f "tokens=1 delims=." %%a in ("!node_ver!") do (
        if %%a geq 16 (echo ✅ Node.js版本符合要求（≥16）) else (echo ❌ Node.js版本过低，需要≥16)
    )
) else (
    echo ❌ 未检测到Node.js
)
echo.

:: 3. 检测 MySQL 服务状态
echo [MySQL status]
netstat -ano | findstr ":3306" | findstr "LISTENING" >nul
if %errorlevel% equ 0 (
    echo ✅ MySQL服务正在运行（监听端口：3306）
    set "mysql_pid="
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":3306" ^| findstr "LISTENING"') do (
        set "mysql_pid=%%a"
    )
    if defined mysql_pid (
        set "process_name="
        for /f "tokens=1,* delims=:" %%b in ('tasklist /fi "PID eq !mysql_pid!" /fo list ^| findstr /i "映像名称 Image"') do (
            set "process_name=%%c"
        )
        if not defined process_name (
            for /f "tokens=1,*" %%b in ('tasklist /fi "PID eq !mysql_pid!" /fo list') do (
                if /i "%%b"=="Image" set "process_name=%%c"
                if /i "%%b"=="映像名称" set "process_name=%%c"
            )
        )
        if defined process_name (
            set "process_name=!process_name: =!"
            echo 进程名称: !process_name!
        ) else (
            echo 进程PID: !mysql_pid!
        )
    )
) else (
    echo ❌ MySQL服务未运行（端口3306无监听）
)
echo.

:: ============================================
:: 检查端口占用情况
:: ============================================
echo [检查端口占用情况]

:: 检查后端端口(假设是3000)
set "BACKEND_PORT=3000"
echo 检查后端端口 %BACKEND_PORT%...
netstat -ano | findstr ":%BACKEND_PORT%" | findstr "LISTENING" >nul
if %errorlevel% equ 0 (
    echo ⚠️ 端口 %BACKEND_PORT% 被占用
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":%BACKEND_PORT%" ^| findstr "LISTENING"') do (
        set "pid=%%a"
        echo 正在终止进程PID: !pid!
        taskkill /f /pid !pid!
    )
    echo ✅ 已释放端口 %BACKEND_PORT%
) else (
    echo ✅ 端口 %BACKEND_PORT% 可用
)
echo.

:: 检查前端端口(8080)
set "FRONTEND_PORT=8080"
echo 检查前端端口 %FRONTEND_PORT%...
netstat -ano | findstr ":%FRONTEND_PORT%" | findstr "LISTENING" >nul
if %errorlevel% equ 0 (
    echo ⚠️ 端口 %FRONTEND_PORT% 被占用
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":%FRONTEND_PORT%" ^| findstr "LISTENING"') do (
        set "pid=%%a"
        echo 正在终止进程PID: !pid!
        taskkill /f /pid !pid!
    )
    echo ✅ 已释放端口 %FRONTEND_PORT%
) else (
    echo ✅ 端口 %FRONTEND_PORT% 可用
)
echo.

:: ============================================
:: 启动后端服务
:: ============================================
echo [启动后端服务]
set "BACKEND_DIR=%ROOT_DIR%"
set "MVN_CMD=mvn"

:: 检查是否使用Maven Wrapper
if exist "%BACKEND_DIR%mvnw.cmd" (
    set "MVN_CMD=mvnw.cmd"
    echo 检测到Maven Wrapper，使用mvnw命令
)

echo 正在安装后端依赖并启动Spring Boot...
if "%SHOW_CONSOLE%"=="1" (
    start "后端服务" cmd /c "cd /d "%BACKEND_DIR%" && %MVN_CMD% spring-boot:run && exit"
) else (
    start "后端服务" /B cmd /c "cd /d "%BACKEND_DIR%" && %MVN_CMD% spring-boot:run && exit"
)
echo ✅ 后端服务启动中...
timeout /t 5 >nul
echo.

:: ============================================
:: 启动前端服务
:: ============================================
echo [启动前端服务]
set "FRONTEND_DIR=%ROOT_DIR%get_better_together_ui"

if not exist "%FRONTEND_DIR%" (
    echo ❌ 前端目录不存在: %FRONTEND_DIR%
    pause
    exit /b 1
)

echo 正在安装前端依赖...
cd /d "%FRONTEND_DIR%"
if exist "node_modules" (
    echo ✅ node_modules已存在，跳过安装
) else (
    npm install
    if %errorlevel% neq 0 (
        echo ❌ npm install 失败
        pause
        exit /b 1
    )
    echo ✅ 前端依赖安装完成
)

echo 正在启动前端开发服务器...
if "%SHOW_CONSOLE%"=="1" (
    start "前端服务" cmd /c "cd /d "%FRONTEND_DIR%" && npm run serve && exit"
) else (
    start "前端服务" /B cmd /c "cd /d "%FRONTEND_DIR%" && npm run serve && exit"
)
echo ✅ 前端服务启动中...
timeout /t 5 >nul
echo.

:: ============================================
:: 完成提示并打开浏览器
:: ============================================
echo ============================================
echo 所有服务已启动!
echo.
echo 正在打开浏览器: http://localhost:%FRONTEND_PORT%
start "" "http://127.0.0.1:%FRONTEND_PORT%"
echo ============================================
timeout /t 2 >nul