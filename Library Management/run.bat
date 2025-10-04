@echo off
echo Starting Library Management System...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo Building project...
call mvn clean compile

if %errorlevel% neq 0 (
    echo Error: Build failed
    pause
    exit /b 1
)

echo.
echo Starting application...
echo Default login: admin / admin123
echo.

call mvn javafx:run

pause
