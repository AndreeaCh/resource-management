:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch install.bat - Installs all the programs
:: needed for the installation of Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

:powershell_activation
ECHO UNINSTALL_0.1 Activate powershell...
FOR /f %%G IN ('powershell Get-ExecutionPolicy') DO (IF "%%G" == "Unrestricted" (GOTO set-path-constants))
powershell Set-ExecutionPolicy Unrestricted

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

SET _JAVA_INSTALL_OPTION=zulu

:: TODO : INSTALL_PATH resolved at runtime
:: resource management install path
SET _INSTALL_PATH=C:\Progra~1\resource-management

:::::::::::::::::::::::::::::::::::::: STOP APPLICATION ::::::::::::::::::::::::::::::::::::::

:stop_services
powershell -command .\stop.bat

:::::::::::::::::::::::::::::::::::::: UNINSTALL JAVA ::::::::::::::::::::::::::::::::::::::

:java_uninstall
ECHO UNINSTALL_1.2 Uninstalling java
::powershell -command choco uninstall %_JAVA_INSTALL_OPTION% -y

::::::::::::::::::::::::::::::::::: UNINSTALL MONGODB ::::::::::::::::::::::::::::::::::::::::

:: TODO : check if the policy change is really necessary at uninstall time

:mongo_uninstall
ECHO UNINSTALL_2.2.1 Briefly set execution policy to 'bypass' so that the uninstallation process doesn't get stuck
powershell Set-ExecutionPolicy Bypass

ECHO UNINSTALL_2.2.2 Uninstalling mongodb...
::powershell -command choco uninstall mongodb -y

ECHO UNINSTALL_2.2.3 Restore execution policy to 'unrestricted'
powershell Set-ExecutionPolicy Unrestricted

:::::::::::::::::::::::::::::::::::::: UNIINSTALL NODEJS ::::::::::::::::::::::::::::::::::::::

:node_uninstall
ECHO UNINSTALL_3.1 Uninstalling nodejs...
::powershell -command choco uninstall nodejs -y


::::::::::::::::::::::::::::::: INSTALL RESOURCE MANAGEMENT APP :::::::::::::::::::::::::::::

:isu_uninstall
ECHO UNINSTALL_4.1 Removing app files...
powershell "Get-ChildItem -Path  '%_INSTALL_PATH%' -Recurse -exclude '%_INSTALL_PATH%\unistall.bat' | Remove-Item -force -recurse"

:::::::::::::::::::::::::::::::::::: POST PROCESSING ::::::::::::::::::::::::::::::::::::::::

:cleaning
:powershell-deactivation
ECHO UNINSTALL_5.0 De-activate powershell...
powershell Set-ExecutionPolicy Restricted

ECHO ON