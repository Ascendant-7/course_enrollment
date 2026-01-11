@echo off
REM ===== Load .env =====
for /f "usebackq tokens=1,* delims==" %%i in (.env) do (
    set %%i=%%j
)

REM ===== Run Flyway command passed as argument =====
if "%1"=="" (
    echo Usage: repair.bat [repair|migrate|info|validate|clean]
    exit /b 1
)

mvnw flyway:%1 ^
    -Dflyway.url=%DB_URL% ^
    -Dflyway.user=%DB_USER% ^
    -Dflyway.password=%DB_PASS% ^
    -Dflyway.locations=%FLYWAY_LOCATIONS%

echo.
echo ===== Flyway %1 Completed =====
pause
