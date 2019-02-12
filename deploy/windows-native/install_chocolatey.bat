:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch install.bat - Installs all the programs
:: needed for the installation of Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF
ECHO CHOCO_INSTALL Started install

:powershell_activation
ECHO INSTALL_0.1 Activate powershell...
FOR /f %%G IN ('powershell Get-ExecutionPolicy') DO (IF "%%G" == "Unrestricted" (GOTO set-path-constants))
powershell Set-ExecutionPolicy Unrestricted

: set-path-constants
SET _CHOCO_VER=0.10.11

:: local paths
SET _SCRIPTS_DIR=.\scripts

:choco_setup
ECHO CHOCO_INSTALL_1 Find if 'chocolatey' is already installed
FOR /f %%G IN ('choco --version') DO (
    IF %%G == %_CHOCO_VER% (
        GOTO cleaning
    )
)

:choco_install
ECHO CHOCO_INSTALL_2 Installing chocolatey...
START %_SCRIPTS_DIR%\chocolatey\installChocolatey.cmd

:cleaning
:powershell-deactivation
ECHO CHOCO_INSTALL_3 De-activate powershell...
powershell Set-ExecutionPolicy Restricted

ECHO CHOCO_INSTALL Install finished
ECHO ON