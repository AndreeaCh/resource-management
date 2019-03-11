:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch install.bat - Installs all the programs
:: needed for the installation of Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF
ECHO DB_INSTALL Started install

:powershell_activation
ECHO INSTALL_0.1 Activate powershell...
FOR /f %%G IN ('powershell Get-ExecutionPolicy') DO (IF "%%G" == "Unrestricted" (GOTO set-path-constants))
powershell Set-ExecutionPolicy Unrestricted

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

:: mongodb constants - modify this as needed
SET _MONGO_SERVER_PATH=C:\Progra~1\MongoDB\Server\4.0
SET _MONGO_DATA_PATH=C:\mongodb\data\db
SET _MONGO_LOG_PATH=C:\mongodb\log
SET _MONGO_VER=4.0.6

::::::::::::::::::::::::::::::::::: MONGODB SETUP ::::::::::::::::::::::::::::::::::::::

:mongo_setup
ECHO DB_INSTALL_1.0 Find if mongodb is installed on this machine
FOR /f "tokens=3" %%G IN ('mongod --version 2^>^&1 ^| findstr /i "db"') DO (
    IF %%G == v%_MONGO_VER% (
        GOTO cleanup
    )
)

:mongo_install
ECHO DB_INSTALL_1.2.1 Briefly set execution policy to 'bypass' so that installation process doesn't get stuck
powershell Set-ExecutionPolicy Bypass

ECHO DB_INSTALL_1.2.2 Installing mongodb...
powershell -command choco install mongodb -y --version %_MONGO_VER% --params="'/dataPath:%_MONGO_DATA_PATH% /logPath:%_MONGO_LOG_PATH%'"

ECHO DB_INSTALL_1.2.3 Restore execution policy to 'unrestricted'
powershell Set-ExecutionPolicy Unrestricted

:mongo_configure
ECHO DB_INSTALL_2.3 Configure mongodb...

ECHO DB_INSTALL 2.3.1 Set custom environment variables
SETX MONGO_HOME %_MONGO_SERVER_PATH% -m

ECHO DB_INSTALL 2.3.2 Add to PATH env variable
ECHO %PATH%|find /i "%_MONGO_SERVER_PATH%\bin">nul || SETX PATH "%PATH%;%_MONGO_SERVER_PATH%\bin" -m

:cleanup

:powershell-deactivation
ECHO DB_INSTALL_3.0 De-activate powershell...
powershell Set-ExecutionPolicy Restricted

ECHO DB_INSTALL Install finished
ECHO ON