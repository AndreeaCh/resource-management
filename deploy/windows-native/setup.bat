:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Deploys and starts the ISU Resource Management application bundle
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

SET _INSTALL_PATH=%1
IF "%_INSTALL_PATH%"=="" SET _INSTALL_PATH=C:\Progra~1\resource-management

SET _ARCHIVE_PATH=.\dist\resource-management-windows-native-bin.zip
SET _LOGS_PATH=%CD%\logs


: set-timestamp
FOR /f "tokens=2 delims==" %%I IN ('wmic os get localdatetime /format:list') DO SET _DATETIME=%%I
SET _DATETIME=%_DATETIME:~0,8%-%_DATETIME:~8,6%

:run_install_script
ECHO SETUP_1 Installing application and its dependencies...
powershell -command "Start-Process powershell -ArgumentList 'cd \"%CD%\"; & .\install.bat %_ARCHIVE_PATH% %_INSTALL_PATH% >> %_LOGS_PATH%\setup-%_DATETIME%.log 2>&1' -Verb runas -Wait -WindowStyle hidden"

:: TODO: disabling for now until we find a way to pass installation paths (either as parameters or env vars)
:start_application
:: ECHO SETUP_2 Launching application and its dependencies...
:: powershell -command "Start-Process powershell -ArgumentList 'cd \"%_INSTALL_PATH%\"; & .\start.bat >> %_LOGS_PATH%\setup-%_DATETIME%.log 2>&1' -WindowStyle hidden"

:cleaning

ECHO SETUP_END Finished setup
ECHO To start the application open a new console and type 'easyman start'

ECHO ON
PAUSE