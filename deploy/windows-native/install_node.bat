:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch install_runners.bat - Installs the programs
:: needed for running the Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF
ECHO NODE_INSTALL Started install

:powershell_activation
ECHO NODE_INSTALL_0.1 Activate powershell...
FOR /f %%G IN ('powershell Get-ExecutionPolicy') DO (IF "%%G" == "Unrestricted" (GOTO set-path-constants))
powershell Set-ExecutionPolicy Unrestricted

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

:: nodejs path constants - modify this as needed
SET _NODE_INSTALL_PATH=C:\Progra~1\nodejs
SET _NODE_VER=10.15.0

:::::::::::::::::::::::::::::::::::::: INSTALL NODEJS ::::::::::::::::::::::::::::::::::::::

:node_setup
ECHO NODE_INSTALL_1 Find if 'nodejs' is already installed
FOR /f %%G IN ('node -v') DO (
    IF %%G == v%_NODE_VER% (
        GOTO node_configure
    )
)

:node_install
ECHO NODE_INSTALL_2 Installing nodejs...
powershell -command choco install nodejs -y --version %_NODE_VER%

ECHO INSTALL_3 Set custom environment variables
SETX NODE_HOME %_NODE_INSTALL_PATH% -m

:::::::::::::::::::::::::::::::::::: POST PROCESSING ::::::::::::::::::::::::::::::::::::::::

:cleanup

:powershell-deactivation
ECHO NODE_INSTALL_5 De-activate powershell...
powershell Set-ExecutionPolicy Restricted

ECHO NODE_INSTALL Install finished

ECHO ON