@echo off
echo =======================================
echo   EarthGuard - Starting All Services
echo =======================================
echo.

echo [1/2] Building all services (this may take a few minutes on first run)...
docker compose -f docker-compose.prod.yml build
if %errorlevel% neq 0 (
    echo BUILD FAILED! Make sure Docker Desktop is running.
    pause
    exit /b 1
)

echo.
echo [2/2] Starting all services...
docker compose -f docker-compose.prod.yml up -d

echo.
echo Waiting for services to start...
echo (This can take 1-2 minutes on first start)
echo.

timeout /t 90 /nobreak > nul

echo.
echo =======================================
echo   EarthGuard is running!
echo =======================================
echo.
echo   Frontend:    http://localhost
echo   API Gateway: http://localhost:8080
echo   Swagger UI:  http://localhost:8081/swagger-ui.html
echo.
echo   Default users:
echo     admin    / admin123  (ADMIN role)
echo     testuser / user123   (USER role)
echo.
echo   Useful commands:
echo     Logs:  docker compose -f docker-compose.prod.yml logs -f
echo     Stop:  docker compose -f docker-compose.prod.yml down
echo =======================================
echo.
pause
