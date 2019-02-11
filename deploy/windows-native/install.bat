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
SET _MONGO_SERVER_PATH=C:\Progra~1\MongoDB\Server\4.0
SET _MONGO_DATA_PATH=C:\mongodb\data\db
SET _MONGO_LOG_PATH=C:\mongodb\log
SET _MONGO_VER=4.0.4

:: openjdk path constants - modify this as needed
SET _JAVA_INSTALL_OPTION=zulu
SET _JAVA_INSTALL_PATH=C:\Progra~1\Zulu\zulu
SET _JAVA_VER=11.29.3

:: nodejs path constants - modify this as needed
SET _NODE_INSTALL_PATH=C:\Progra~1\nodejs
SET _NODE_VER=10.15.0
SET _HTTP_SERVER_VER=0.11.1

:: local paths
SET _SCRIPTS_DIR=.\scripts
SET _TEMP_DIR=.\tmp

ECHO INSTALL_0.1 : Create temp folder...
IF NOT EXIST %_TEMP_DIR% MD %_TEMP_DIR%

:: intermediary path
SET _new_path=%PATH%

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
:: TODO: the rest of the script shall not inherit this shell state, choco will not be available in the same shell session

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
powershell -command choco install %_JAVA_INSTALL_OPTION% -y --version %_JAVA_VER%

:java_configure
ECHO INSTALL_1.3 Configure java...

ECHO INSTALL 1.3.1 Set custom environment variables
SETX JAVA_HOME %_JAVA_INSTALL_PATH% -m

ECHO INSTALL 1.3.2 Add to PATH env variable
ECHO %PATH%|find /i "%_JAVA_INSTALL_PATH%\bin">nul || SET _new_path=%_new_path%;%_JAVA_INSTALL_PATH%\bin
SETX PATH %_new_path% -m

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

:mongo_configure
ECHO INSTALL_2.3 Configure mongodb...

ECHO INSTALL 2.3.1 Set custom environment variables
SETX MONGO_HOME %_MONGO_SERVER_PATH% -m

ECHO INSTALL 2.3.2 Add to PATH env variable
ECHO %PATH%|find /i "%_MONGO_SERVER_PATH%\bin">nul || SET _new_path=%_new_path%;%_MONGO_SERVER_PATH%\bin
SETX PATH %_new_path% -m

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
ECHO INSTALL_3.2 Configure nodejs...

ECHO INSTALL 3.3.1 Set custom environment variables
SETX NODE_HOME %_NODE_INSTALL_PATH% -m

ECHO INSTALL 3.3.2 Add to PATH env variable
ECHO %PATH%|find /i "%_NODE_INSTALL_PATH%">nul || SET _new_path=%_new_path%;%_NODE_INSTALL_PATH%
SETX PATH %_new_path% -m

ECHO INSTALL 4.1 check if http-server is installed

FOR /f "tokens=2" %%G IN ('%_NODE_INSTALL_PATH%\npm list -g --depth=0 2^>^&1 ^| findstr /i "http-server"') DO (
    IF %%G == http-server@%_HTTP_SERVER_VER% (
        GOTO http_server_configure
    )
)

:http_server_install
ECHO INSTALL_4.2 Installing http server
powershell -command %_NODE_INSTALL_PATH%\npm install -g http-server

:http_server_configure
ECHO INSTALL_4.3 Configure http server... skipping


:::::::::::::::::::::::::::::::::: INSTALL COMPRESSION APP ::::::::::::::::::::::::::::::::::

:7z_setup
ECHO INSTALL_5.0 Installing 7-zip
powershell -command choco install 7zip -y

::::::::::::::::::::::::::::::: INSTALL RESOURCE MANAGEMENT APP :::::::::::::::::::::::::::::

:: TODO: find if application is already installed and remove all files

:isu_install
ECHO INSTALL_6.0 Creating install folder
MKDIR "%_INSTALL_PATH%" 2>nul
IF NOT EXIST "%_INSTALL_PATH%\*" (
    ECHO Failed to create directory "%_INSTALL_PATH%"
    GOTO :cleaning
)

ECHO INSTALL_6.1 Extracting archive...
7z x %_ARCHIVE_PATH% -o%_TEMP_DIR% -aoa -y -r

ECHO INSTALL_6.2 : Move extracted files to instalation directory ...
FOR /f %%G IN ('dir /A:D /B %_TEMP_DIR%') DO xcopy .\%_TEMP_DIR%\%%G %_INSTALL_PATH% /e /y /i
::powershell -command Expand-Archive -LiteralPath %_ARCHIVE_PATH% -DestinationPath %_INSTALL_PATH%
::tar -xf %_ARCHIVE_PATH% -C %_INSTALL_PATH% --strip-components=1

ECHO INSTALL 6.3 Change folder permissions to allow normal user access
icacls %_INSTALL_PATH% /q /c /t /grant Users:F

ECHO INSTALL 6.4 Set custom environment variables
SETX EASYMAN_HOME %_INSTALL_PATH% -m

:add_app_bin_to_path
ECHO INSTALL 6.5 Setting PATH env variable
ECHO %PATH%|find /i "%_INSTALL_PATH%\bin">nul || SET _new_path=%_new_path%;%_INSTALL_PATH%\bin
SETX PATH %_new_path% -m

:::::::::::::::::::::::::::::::::::: POST PROCESSING ::::::::::::::::::::::::::::::::::::::::

:cleaning
:powershell-deactivation
ECHO INSTALL_6.0 De-activate powershell...
powershell Set-ExecutionPolicy Restricted

ECHO INSTALL_6.1 : Remove any temporary files ...
RD /S /Q %_TEMP_DIR%

ECHO ON