:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch start.bat - Starts the ISU Resource Management application bundle
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

SET _INSTALL_PATH=%EASYMAN_HOME%
IF "%_INSTALL_PATH%"=="" (
    ECHO Install path is NOT defined
    GOTO :cleaning
)

SET _SCRIPTS_DIR=%_INSTALL_PATH%\scripts
SET _LOGS_DIR=%_INSTALL_PATH%\logs

SET _BACKEND_PATH=%_INSTALL_PATH%\jars\easy-manage.jar
SET _FRONTEND_PATH=%_INSTALL_PATH%\dist
SET _HTTP_SERVER_VER=0.11.1

SET _CONFIG_PATH=%_INSTALL_PATH%\application.properties

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

IF "%MONGO_HOME%"=="" (
    ECHO MongoDb install path is NOT defined
    GOTO :cleaning
)
SET _MONGO_BIN_PATH=%MONGO_HOME%/bin

: set-timestamp
FOR /f "tokens=2 delims==" %%I IN ('wmic os get localdatetime /format:list') DO SET _DATETIME=%%I
SET _DATETIME=%_DATETIME:~0,8%-%_DATETIME:~8,6%

:::::::::::::::::::::::::::::::::::: START DB SERVER ::::::::::::::::::::::::::::::::::

:check_mongod
ECHO START_1.0 Verify if mongod has already started
FOR /F "tokens=1,2" %%G IN ('tasklist /FI "IMAGENAME eq mongod.exe" /fo table /nh') DO (
    IF %%H NEQ No (
        GOTO start_backend
    )
)

:start_mongod
ECHO START_1.1 Start mongo
powershell -command "Start-Process powershell -ArgumentList '%_MONGO_BIN_PATH%\mongod --config %_MONGO_BIN_PATH%\mongod.cfg  >> %_LOGS_DIR%\mongod-%_DATETIME%.log 2>&1' -WindowStyle hidden"

ECHO START_1.2 Waiting for the daemon to start...
timeout 15

:::::::::::::::::::::::::::::::::::: START BACKEND :::::::::::::::::::::::::::::::::::::

:start_backend
ECHO START_2.1 Verify if backend application has already started
FOR /F "tokens=1,2" %%G IN ('tasklist /FI "IMAGENAME eq java.exe" /fo table /nh') DO (
    IF %%H NEQ No (
        GOTO start_frontend
    )
)

ECHO START_2.2 Starting new server instance...
powershell -command "Start-Process powershell -ArgumentList 'cd \"%_SCRIPTS_DIR%\"; & .\run-backend.bat %_BACKEND_PATH% %_CONFIG_PATH% >> %_LOGS_DIR%\backend-%_DATETIME%.log 2>&1' -WindowStyle hidden"


:::::::::::::::::::::::::::::::::::: START FRONTEND :::::::::::::::::::::::::::::::::::::

:start_frontend

:node_configure
ECHO START_3.0.1 check if http-server is installed
FOR /f "tokens=2" %%G IN ('npm list -g --depth=0 2^>^&1 ^| findstr /i "http-server"') DO (
    IF %%G == http-server@%_HTTP_SERVER_VER% (
        GOTO http_server_configure
    )
)

:http_server_install
ECHO START_3.0.2 Installing http server
powershell -command npm install -g http-server

:http_server_configure
ECHO START_3.0.3 Configure http server... skipping

ECHO START_3.1 Verify if frontend application has already started
FOR /F "tokens=1,2" %%G IN ('tasklist /FI "IMAGENAME eq node.exe" /fo table /nh') DO (
    IF %%H NEQ No (
        GOTO cleaning
    )
)

ECHO START_3.2 Starting new client instance...
powershell -command "Start-Process powershell -ArgumentList 'http-server %_FRONTEND_PATH% -p 8080 >> %_LOGS_DIR%\frontend-%_DATETIME%.log 2>&1' -WindowStyle hidden"

::::::::::::::::::::::::::::::::::: POST PROCESSING :::::::::::::::::::::::::::::::::::::

:cleaning
ECHO ON
