:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch uninstall_tools.bat - Uninstalls all of the dependent tools
:: for the installation of Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF
ECHO UNINSTALL_TOOLS_START Started uninstall

:powershell_activation
ECHO UNINSTALL_0 Activate powershell...
FOR /f %%G IN ('powershell Get-ExecutionPolicy') DO (IF "%%G" == "Unrestricted" (GOTO set-path-constants))
powershell Set-ExecutionPolicy Unrestricted

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

SET _JAVA_INSTALL_OPTION=zulu

::::::::::::::::::::::::::::::::: UNINSTALL PROGRAMS ::::::::::::::::::::::::::::::::::

:java_uninstall
ECHO UNINSTALL_TOOLS_1 Uninstalling java
powershell -command choco uninstall %_JAVA_INSTALL_OPTION% -y

:node_uninstall
ECHO UNINSTALL_TOOLS_2 Uninstalling nodejs...
powershell -command choco uninstall nodejs -y

:mongo_uninstall
ECHO UNINSTALL_TOOLS_3.1 Stopping db server ...
TASKKILL /IM mongod.exe /F

ECHO UNINSTALL_TOOLS_3.2 Uninstalling mongodb...
powershell -command choco uninstall mongodb -y

:::::::::::::::::::::::::::::::::::: POST PROCESSING :::::::::::::::::::::::::::::::::::

:cleaning
:powershell-deactivation
ECHO UNINSTALL_TOOLS_4.0 De-activate powershell...
powershell Set-ExecutionPolicy Restricted

ECHO UNINSTALL_TOOLS_END Finished uninstalling tools
ECHO ON