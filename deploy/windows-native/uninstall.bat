:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch install.bat - Installs all the programs
:: needed for the installation of Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

:powershell_activation
ECHO INSTALL_0.1 Activate powershell...
FOR /f %%G IN ('powershell Get-ExecutionPolicy') DO (IF "%%G" == "Unrestricted" (GOTO set-path-constants))
powershell Set-ExecutionPolicy Unrestricted

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

:: TODO : INSTALL_PATH resolved at runtime
:: resource management install path
SET _INSTALL_PATH=C:\Progra~1\resource-management


:::::::::::::::::::::::::::::::::::::: UNINSTALL JAVA ::::::::::::::::::::::::::::::::::::::

:java_uninstall
ECHO INSTALL_1.2 Uninstalling java
powershell -command choco uninstall zulu -y

::::::::::::::::::::::::::::::::::: UNINSTALL MONGODB ::::::::::::::::::::::::::::::::::::::::

:mongo_uninstall
ECHO INSTALL_2.2.1 Briefly set execution policy to 'bypass' so that installation process doesn't get stuck
powershell Set-ExecutionPolicy Bypass

ECHO INSTALL_2.2.2 Uninstalling mongodb...
powershell -command choco uninstall mongodb -y

ECHO INSTALL_2.2.3 Restore execution policy to 'unrestricted'
powershell Set-ExecutionPolicy Unrestricted

:::::::::::::::::::::::::::::::::::::: UNIINSTALL NODEJS ::::::::::::::::::::::::::::::::::::::

:node_uninstall
ECHO INSTALL_3.1 Installing nodejs...
powershell -command choco uninstall nodejs -y


::::::::::::::::::::::::::::::: INSTALL RESOURCE MANAGEMENT APP :::::::::::::::::::::::::::::

:isu_uninstall
ECHO INSTALL_4.1 Removing app files...
:: TODO


:::::::::::::::::::::::::::::::::::: POST PROCESSING ::::::::::::::::::::::::::::::::::::::::

:cleaning
:powershell-deactivation
ECHO INSTALL_5.0 De-activate powershell...
powershell Set-ExecutionPolicy Restricted

ECHO ON