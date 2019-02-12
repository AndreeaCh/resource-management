:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch install_app.bat - Installs the Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF
ECHO APP_INSTALL Started install

:powershell_activation
ECHO APP_INSTALL_0.1 Activate powershell...
FOR /f %%G IN ('powershell Get-ExecutionPolicy') DO (IF "%%G" == "Unrestricted" (GOTO set-path-constants))
powershell Set-ExecutionPolicy Unrestricted

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

:: resource management install path
SET _ARCHIVE_PATH=%1
SET _INSTALL_PATH=%2

IF "%_ARCHIVE_PATH%"=="" (
    ECHO Archive path is NOT defined
    GOTO :cleanup
)

IF "%_INSTALL_PATH%"=="" (
    ECHO Install path is NOT defined
    GOTO :cleanup
)

:: local paths
SET _TEMP_DIR=.\tmp

ECHO APP_INSTALL_0.1 : Create temp folder...
IF NOT EXIST %_TEMP_DIR% MD %_TEMP_DIR%

:: intermediary path
SET _new_path=%PATH%

::::::::::::::::::::::::::::::: INSTALL RESOURCE MANAGEMENT APP :::::::::::::::::::::::::::::

:isu_install

ECHO APP_INSTALL_0 Creating install folder

MKDIR "%_INSTALL_PATH%" 2>nul
IF NOT EXIST "%_INSTALL_PATH%\*" (
    ECHO Failed to create directory "%_INSTALL_PATH%"
    GOTO :cleanup
)

ECHO APP_INSTALL_1 Extracting archive...
7z x %_ARCHIVE_PATH% -o%_TEMP_DIR% -aoa -y -r

ECHO APP_INSTALL_2 : Move extracted files to instalation directory ...
FOR /f %%G IN ('dir /A:D /B %_TEMP_DIR%') DO xcopy .\%_TEMP_DIR%\%%G %_INSTALL_PATH% /e /y /i

ECHO INSTALL 3 Change folder permissions to allow normal user access
icacls %_INSTALL_PATH% /q /c /t /grant Users:F

ECHO INSTALL 4 Set custom environment variables
SETX EASYMAN_HOME %_INSTALL_PATH% -m

:add_app_bin_to_path
ECHO INSTALL 5 Setting PATH env variable
ECHO %PATH%|find /i "%_INSTALL_PATH%\bin">nul || SETX PATH "%PATH%;%_INSTALL_PATH%\bin" -m

:::::::::::::::::::::::::::::::::::: POST PROCESSING ::::::::::::::::::::::::::::::::::::::::

:cleanup

:powershell-deactivation
ECHO APP_INSTALL_6.0 De-activate powershell...
powershell Set-ExecutionPolicy Restricted

ECHO APP_INSTALL_6.1 : Remove any temporary files ...
RD /S /Q %_TEMP_DIR%

ECHO APP_INSTALL Install finished
ECHO ON