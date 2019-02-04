:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch install.bat - Installs all the programs
:: needed for the installation of Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

:::::::::::::::::::::::::::::::: INSTALLED PROGRAMS :::::::::::::::::::::::::::::::::::

:powershell_activation
ECHO INSTALL_0.1 Activate powershell...
FOR /f %%G IN ('powershell Get-ExecutionPolicy') DO (IF "%%G" == "Unrestricted" (GOTO set-path-constants))
powershell Set-ExecutionPolicy Unrestricted

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

:: resource management install path
SET _ARCHIVE_PATH=%1
SET _INSTALL_PATH=%2

IF "%_ARCHIVE_PATH%"=="" (
    ECHO Archive path is NOT defined
    GOTO :cleaning
)

IF "%_INSTALL_PATH%"=="" (
    ECHO Install path is NOT defined
    GOTO :cleaning
)

:: chocolatey constants
SET _CHOCO_VER=0.10.11

:: mongodb constants - modify this as needed
SET _MONGO_BIN_PATH=C:\Progra~1\MongoDB\Server\4.0\bin
SET _MONGO_DATA_PATH=C:\mongodb\data\db
SET _MONGO_LOG_PATH=C:\mongodb\log
SET _MONGO_VER=4.0.4

:: openjdk path constants - modify this as needed
SET _JAVA_INSTALL_PATH=C:\Progra~1\Zulu\zulu
SET _JAVA_VER=11.0.1

:: nodejs path constants - modify this as needed
SET _NODE_INSTALL_PATH=C:\Progra~1\nodejs
SET _NODE_VER=10.15.0
SET _HTTP_SERVER_VER=0.11.1

:::::::::::::::::::::::::::::::::::::: INSTALL CHOCOLATEY ::::::::::::::::::::::::::::::::::::::

:choco_setup
ECHO INSTALL_0.4 Find if 'chocolatey' is already installed
FOR /f %%G IN ('choco --version') DO (
    IF %%G == %_CHOCO_VER% (
        GOTO choco_configure
    )
)

:choco_install
ECHO INSTALL_0.5 Installing chocolatey...
START %_SCRIPTS_DIR%\chocolatey\installChocolatey.cmd

:choco_configure
ECHO INSTALL_0.6 Configure chocolatey... skipping

:::::::::::::::::::::::::::::::::::::: INSTALL JAVA ::::::::::::::::::::::::::::::::::::::

:java_setup
ECHO INSTALL_1.0 Find if 'java' is already installed
FOR /f "tokens=3" %%G IN ('java -version 2^>^&1 ^| findstr /i "version"') DO (
    IF %%G == "%_JAVA_VER%" (
        GOTO java_configure
    )
)

:java_install
ECHO INSTALL_1.2 Installing java if not already installed...
powershell -command choco install zulu -y --version %_JAVA_VER%

:java_configure
ECHO INSTALL_1.3 Configure java... skipping

::::::::::::::::::::::::::::::::::: MONGODB SETUP ::::::::::::::::::::::::::::::::::::::::

:mongo_setup
ECHO INSTALL_2.0 Find if mongodb is installed on this machine
FOR /f "tokens=3" %%G IN ('mongod --version 2^>^&1 ^| findstr /i "db"') DO (
    IF %%G == v%_MONGO_VER% (
        GOTO mongo_configure
    )
)

:mongo_install
ECHO INSTALL_2.2.1 Briefly set execution policy to 'bypass' so that installation process doesn't get stuck
powershell Set-ExecutionPolicy Bypass

ECHO INSTALL_2.2.2 Installing mongodb...
powershell -command choco install mongodb -y --version %_MONGO_VER% --params="'/dataPath:%_MONGO_DATA_PATH% /logPath:%_MONGO_LOG_PATH%'"

ECHO INSTALL_2.2.3 Restore execution policy to 'unrestricted'
powershell Set-ExecutionPolicy Unrestricted

:: TODO : the path should no longer contain the bin directory of previous installations
ECHO INSTALL_2.2.3 Add bin directory to path variable
setx /m PATH "%PATH%;%_MONGO_BIN_PATH%"

:mongo_configure
ECHO INSTALL_2.3 Configure mongodb... skipping

:::::::::::::::::::::::::::::::::::::: INSTALL NODEJS ::::::::::::::::::::::::::::::::::::::

:node_setup
ECHO INSTALL_3.0 Find if 'nodejs' is already installed
FOR /f %%G IN ('node -v') DO (
    IF %%G == v%_NODE_VER% (
        GOTO node_configure
    )
)

:node_install
ECHO INSTALL_3.1 Installing nodejs...
powershell -command choco install nodejs -y --version %_NODE_VER%

:node_configure
ECHO INSTALL_3.2 Configure nodejs... check if http-server is installed

FOR /f "tokens=2" %%G IN ('npm list -g --depth=0 2^>^&1 ^| findstr /i "http-server"') DO (
    IF %%G == http-server@%_HTTP_SERVER_VER% (
        GOTO http_server_configure
    )
)

:http_server_install
ECHO INSTALL_3.3 Installing http server
powershell -command npm install -g http-server

:http_server_configure
ECHO INSTALL_3.4 Configure http server... skipping


::::::::::::::::::::::::::::::: INSTALL RESOURCE MANAGEMENT APP :::::::::::::::::::::::::::::

:: TODO: find if application is already installed and remove all files

:isu_install
ECHO INSTALL_4.0 Creating install folder
mkdir "%_INSTALL_PATH%" 2>nul
IF NOT EXIST "%_INSTALL_PATH%\*" (
    ECHO Failed to create directory "%_INSTALL_PATH%"
    GOTO :cleaning
)

ECHO INSTALL_4.1 Extracting archive...
tar -xf %_ARCHIVE_PATH% -C %_INSTALL_PATH% --strip-components=1

:::::::::::::::::::::::::::::::::::: POST PROCESSING ::::::::::::::::::::::::::::::::::::::::

:cleaning
:powershell-deactivation
ECHO INSTALL_5.0 De-activate powershell...
powershell Set-ExecutionPolicy Restricted

ECHO ON