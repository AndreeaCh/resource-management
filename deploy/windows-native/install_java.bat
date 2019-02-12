:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch install_runners.bat - Installs the programs
:: needed for running the Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF
ECHO JAVA_INSTALL Started install

:powershell_activation
ECHO JAVA_INSTALL_0.1 Activate powershell...
FOR /f %%G IN ('powershell Get-ExecutionPolicy') DO (IF "%%G" == "Unrestricted" (GOTO set-path-constants))
powershell Set-ExecutionPolicy Unrestricted

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

:: java path constants - modify this as needed
SET _JAVA_INSTALL_OPTION=zulu
SET _JAVA_INSTALL_PATH=C:\Progra~1\Zulu\zulu
SET _JAVA_VER=11.29.3

:::::::::::::::::::::::::::::::::::::: INSTALL JAVA ::::::::::::::::::::::::::::::::::::::

:java_setup
ECHO JAVA_INSTALL_1.0 Find if 'java' is already installed
FOR /f "tokens=3" %%G IN ('java -version 2^>^&1 ^| findstr /i "version"') DO (
    IF %%G == "%_JAVA_VER%" (
        GOTO java_configure
    )
)

:java_install
ECHO JAVA_INSTALL_1.2 Installing java if not already installed...
powershell -command choco install %_JAVA_INSTALL_OPTION% -y --version %_JAVA_VER%

:java_configure
ECHO JAVA_INSTALL_1.3 Configure java...

:: optional, can be removed
ECHO JAVA_INSTALL_1.3.1 Set custom environment variables
SETX JAVA_HOME %_JAVA_INSTALL_PATH% -m

:::::::::::::::::::::::::::::::::: INSTALL COMPRESSION APP ::::::::::::::::::::::::::::::::::

:7z_setup
ECHO JAVA_INSTALL_2 Installing 7-zip
powershell -command choco install 7zip -y

:::::::::::::::::::::::::::::::::::: POST PROCESSING ::::::::::::::::::::::::::::::::::::::::

:cleanup

:powershell-deactivation
ECHO JAVA_INSTALL_3 De-activate powershell...
powershell Set-ExecutionPolicy Restricted

ECHO JAVA_INSTALL Install finished

ECHO ON