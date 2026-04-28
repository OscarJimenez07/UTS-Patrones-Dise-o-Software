@echo off
REM Reconstruye el frontend, lo copia al backend y reempaqueta el JAR.
REM Uso:  rebuild.bat
setlocal

set ROOT=%~dp0
set MVN=%ROOT%tools\apache-maven-3.9.15\bin\mvn.cmd

echo ^>^>^> [1/3] Compilando frontend...
cd /d "%ROOT%frontend"
call npm run build || goto :error

echo ^>^>^> [2/3] Copiando bundle al backend...
if exist "%ROOT%backend\src\main\resources\static" rmdir /s /q "%ROOT%backend\src\main\resources\static"
mkdir "%ROOT%backend\src\main\resources\static"
xcopy /s /e /q "dist\*" "%ROOT%backend\src\main\resources\static\" || goto :error

echo ^>^>^> [3/3] Empaquetando JAR...
cd /d "%ROOT%backend"
call "%MVN%" -q -DskipTests package || goto :error

echo.
echo Listo. Para correrlo:
echo    java -jar backend\target\gameengine.jar
exit /b 0

:error
echo.
echo *** Fallo el rebuild ***
exit /b 1
