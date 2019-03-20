:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Setup scipt for ISU Resource Management application bundle
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

@echo off

SET _INSTALL_PATH=C:\easy-manage
SET _INSTALL_KIT_PATH=%~dp0
SET _ARCHIVE_PATH=%_INSTALL_KIT_PATH%\dist\easy-manage-windows-native-bin.zip
SET _SERVER_PATH=%~2
SET _LOGS_PATH=%CD%\logs
SET _SCRIPTS_PATH=%CD%\scripts

: set-timestamp
FOR /f "tokens=2 delims==" %%I IN ('wmic os get localdatetime /format:list') DO SET _DATETIME=%%I
SET _DATETIME=%_DATETIME:~0,8%-%_DATETIME:~8,6%

if "%1%"=="prereq" (
   ECHO SETUP Installing chocolatey package manager...
   powershell -command "Start-Process powershell -ArgumentList 'cd \"%_SCRIPTS_PATH%\"; & .\install_chocolatey.bat >> %_LOGS_PATH%\%_DATETIME%-setup-chocolatey.log 2>&1' -Verb runas -Wait -WindowStyle hidden"
)
if "%1%"=="db" (
   ECHO SETUP Installing db...
   powershell -command "Start-Process powershell -ArgumentList 'cd \"%_SCRIPTS_PATH%\"; & .\install_db.bat >> %_LOGS_PATH%\%_DATETIME%-setup-db.log 2>&1' -Verb runas -Wait -WindowStyle hidden"
)

if "%1%"=="java" (
   ECHO SETUP Installing java...
   powershell -command "Start-Process powershell -ArgumentList 'cd \"%_SCRIPTS_PATH%\"; & .\install_java.bat >> %_LOGS_PATH%\%_DATETIME%-setup-java.log 2>&1' -Verb runas -Wait -WindowStyle hidden"
)

if "%1%"=="node" (
   ECHO SETUP Installing node...
   powershell -command "Start-Process powershell -ArgumentList 'cd \"%_SCRIPTS_PATH%\"; & .\install_node.bat >> %_LOGS_PATH%\%_DATETIME%-setup-node.log 2>&1' -Verb runas -Wait -WindowStyle hidden"
)

if "%1%"=="app" (
   ECHO SETUP Installing application...
   powershell -command "Start-Process powershell -ArgumentList 'cd \"%_SCRIPTS_PATH%\"; & .\install_app.bat %_ARCHIVE_PATH% %_INSTALL_PATH% %_SERVER_PATH% >> %_LOGS_PATH%\%_DATETIME%-setup-app.log 2>&1' -Verb runas -Wait -WindowStyle hidden"
   ECHO To start the application open a new console and type 'easymanage start'
)

if "%1%"=="launch" (
   ECHO SETUP Launching application and its dependencies...
   powershell -command "Start-Process powershell -ArgumentList 'cd \"%_INSTALL_PATH%\"; & .\start.bat >> %_LOGS_PATH%\%_DATETIME%-launch.log 2>&1' -WindowStyle hidden"
)

if "%1%"=="help" (
    echo Usage: setup.bat "[option]"
    echo Options:
    echo    prereq                 - install chocolatey
    echo    db                     - install database tool
    echo    java                   - install runtime
    echo    node                   - install nodejs
    echo    app [server_address]   - install application
    echo    help                   - show help
)

if "%~1%"=="" (
    echo Usage: setup.bat "[option]"
    echo Options:
    echo    prereq                 - install chocolatey
    echo    db                     - install database tool
    echo    java                   - install runtime
    echo    node                   - install nodejs
    echo    app [server_address]   - install application
    echo    help                   - show help
)

:cleaning

ECHO SETUP_END Finished setup, press any key to exit

ECHO ON

if "%2%" NEQ "/S" (
 PAUSE
)
