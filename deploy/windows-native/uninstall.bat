:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch install.bat - Installs all the programs
:: needed for the installation of Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF
ECHO UNINSTALL_START Started uninstall

::::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::

: set-path-constants
SET _UNINSTALL_OPTION=%1

IF "%EASYMAN_HOME%"=="" (
    ECHO Install path is NOT defined
    GOTO :cleaning
)

:: resource management install path
SET _INSTALL_PATH=%EASYMAN_HOME%

: set-timestamp
FOR /f "tokens=2 delims==" %%I IN ('wmic os get localdatetime /format:list') DO SET _DATETIME=%%I
SET _DATETIME=%_DATETIME:~0,8%-%_DATETIME:~8,6%

:::::::::::::::::::::::::::::::::: STOP APPLICATION :::::::::::::::::::::::::::::::::::

:stop_services
ECHO UNINSTALL_0.1 Stopping applications
powershell -command easyman stop

:::::::::::::::::::::::::::::: OPTIONALLY UNINSTALL DEPENDENCIES ::::::::::::::::::::::

IF "%_UNINSTALL_OPTION%"=="all" (
    ECHO UNINSTALL_1.0 Uninstalling dependencies
    powershell -command "Start-Process powershell -ArgumentList 'cd \"%_INSTALL_PATH%\"; & .\uninstall_tools.bat >> %_LOGS_PATH%\uninstall-%_DATETIME%.log 2>&1' -Verb runas -Wait -WindowStyle hidden"
)

:::::::::::::::::::::::::: UNINSTALL RESOURCE MANAGEMENT APP ::::::::::::::::::::::::::

:isu_uninstall
ECHO UNINSTALL_4.1 Removing app files...
powershell "Get-ChildItem -Path  '%_INSTALL_PATH%' -Recurse -exclude '%_INSTALL_PATH%\unistall.bat' | Remove-Item -force -recurse"

:::::::::::::::::::::::::::::::::::: POST PROCESSING ::::::::::::::::::::::::::::::::::

:cleaning

ECHO UNINSTALL_END Finished uninstalling
ECHO ON

PAUSE