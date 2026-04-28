@echo off
chcp 65001 > nul
setlocal

:: 取得 bat 檔所在目錄，往上5層找到 CampKeeper 根目錄
:: %0    = bat 檔案的完整路徑（含檔名）
:: %~d0  = 只取磁碟機代號，例如 C:
:: %~p0  = 只取路徑（不含檔名），例如 \Users\User\...\docker\
:: %~dp0 = 磁碟機 + 路徑 = C:\Users\User\...\docker\

set "SCRIPT_DIR=%~dp0"
set "ROOT_DIR=%SCRIPT_DIR%..\..\..\..\.."
set "COMPOSE_FILE=%SCRIPT_DIR%docker-compose.yml"

:: 統一 context 到根目錄執行
cd /d "%ROOT_DIR%"

echo ========================================
echo  CampKeeper Docker Manager
echo  Root: %ROOT_DIR%
echo ========================================
echo.
echo  [1] up       - 啟動所有服務
echo  [2] down     - 停止所有服務
echo  [3] build    - 重新建置 image
echo  [4] logs     - 查看 log
echo  [5] restart  - 重啟所有服務
echo  [6] ps       - 查看容器狀態
echo  [0] exit     - 離開
echo.
set /p choice=請選擇操作 :

if "%choice%"=="1" goto up
if "%choice%"=="2" goto down
if "%choice%"=="3" goto build
if "%choice%"=="4" goto logs
if "%choice%"=="5" goto restart
if "%choice%"=="6" goto ps
if "%choice%"=="0" goto end

:up
echo.
echo 啟動服務中...
docker compose -f "%COMPOSE_FILE%" up -d
goto end

:down
echo.
echo 停止服務中...
docker compose -f "%COMPOSE_FILE%" down
goto end

:build
echo.
echo 重新建置 image...
docker compose -f "%COMPOSE_FILE%" build --no-cache
goto end

:logs
echo.
echo 顯示 log (Ctrl+C 離開)...
docker compose -f "%COMPOSE_FILE%" logs -f
goto end

:restart
echo.
echo 重啟服務中...
docker compose -f "%COMPOSE_FILE%" restart
goto end

:ps
echo.
docker compose -f "%COMPOSE_FILE%" ps
goto end

:end
echo.
pause
endlocal