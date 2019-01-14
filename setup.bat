:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Deploys and starts the ISU Resource Management application bundle
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

SET _INSTALL_DIR=%1
SET _ARCHIVE_PATH=%2

IF "%_INSTALL_DIR%"=="" SET _INSTALL_DIR=.\..\test-install\resource-management
IF "%_ARCHIVE_PATH%"=="" SET _ARCHIVE_PATH=.\target\resource-management-0.0.1-SNAPSHOT-bin.zip

: set-timestamp
FOR /f "tokens=2 delims==" %%I IN ('wmic os get localdatetime /format:list') DO SET _DATETIME=%%I
SET _DATETIME=%_DATETIME:~0,8%-%_DATETIME:~8,6%

:extract_distributable
ECHO SETUP_0 Extracting archive...
powershell -command "Start-Process powershell -ArgumentList 'cd \"%CD%\"; & .\scripts\extract_archive.bat %_ARCHIVE_PATH% %_INSTALL_DIR% >> setup-%_DATETIME%.log 2>&1' -Verb runas -Wait -WindowStyle hidden"

:run_install_script
ECHO SETUP_1 Installing application and its dependencies...
powershell -command "Start-Process powershell -ArgumentList 'cd \"%CD%\"; & %_INSTALL_DIR%\install.bat >> setup-%_DATETIME%.log 2>&1' -Verb runas -Wait -WindowStyle hidden"

:start_application
ECHO SETUP_2 Launching application and its dependencies...
powershell -command "Start-Process powershell -ArgumentList 'cd \"%_INSTALL_DIR%\"; & .\start.bat >> %CD%\setup-%_DATETIME%.log 2>&1' -WindowStyle hidden"

:cleaning

ECHO SETUP_END Finished setup...

ECHO ON
PAUSE