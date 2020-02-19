:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch start.bat - Starts the ISU Resource Management application bundle
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

ECHO Reading configuration '%UserProfile%\easymanage.conf'
For /F "tokens=1* delims==" %%A IN (%UserProfile%\easymanage.conf) DO (

    IF "%%A"=="INSTALL_PATH" set _INSTALL_PATH=%%B

    IF "%%A"=="INSTALLED_VERSION" set _INSTALLED_VERSION=%%B

    IF "%%A"=="SERVER_ADDRESS" set _SERVER_ADDRESS=%%B

    IF "%%A"=="SERVER_PORT" set _BACKEND_SERVER_PORT=%%B

    IF "%%A"=="FRONTEND_SERVER_PORT" set _FRONTEND_SERVER_PORT=%%B

    IF "%%A"=="MONGO_HOME" set _MONGO_HOME=%%B

    IF "%%A"=="MONGO_DATA_PATH" set _MONGO_DATA_PATH=%%B

    IF "%%A"=="MONGO_LOG_PATH" set _MONGO_LOG_PATH=%%B

    IF "%%A"=="DB_SERVER_PORT" set _DB_SERVER_PORT=%%B

    IF "%%A"=="NODE_HOME" set _NODE_HOME=%%B

    IF "%%A"=="JAVA_HOME" set _JAVA_HOME=%%B

    IF "%%A"=="AUTH_HOME" set _AUTH_HOME=%%B

    IF "%%A"=="AUTH_SERVER_PORT" set _AUTH_SERVER_PORT=%%B

    IF "%%A"=="HTTP_SERVER_VERSION" set _HTTP_SERVER_VER=%%B
)

ECHO Configured INSTALL_PATH is '%_INSTALL_PATH%'
ECHO Configured INSTALLED_VERSION is '%_INSTALLED_VERSION%'
ECHO Configured MONGO_HOME is '%_MONGO_HOME%'
ECHO Configured MONGO_DATA_PATH is '%_MONGO_DATA_PATH%'
ECHO Configured MONGO_LOG_PATH is '%_MONGO_LOG_PATH%'
ECHO Configured DB_SERVER_PORT is '%_DB_SERVER_PORT%'
ECHO Configured JAVA_HOME is '%_JAVA_HOME%'
ECHO Configured NODE_HOME is '%_NODE_HOME%'
ECHO Configured AUTH_HOME is '%_AUTH_HOME%'
ECHO Configured SERVER_ADDRESS is '%_SERVER_ADDRESS%'
ECHO Configured BACKEND_SERVER_PORT is '%_BACKEND_SERVER_PORT%'
ECHO Configured FRONTEND_SERVER_PORT is '%_FRONTEND_SERVER_PORT%'
ECHO Configured AUTH_SERVER_PORT is '%_AUTH_SERVER_PORT%'
ECHO Configured HTTP_SERVER_VERSION is '%_HTTP_SERVER_VER%'

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

IF "%_INSTALL_PATH%"=="" (
    ECHO Install path is NOT defined
    GOTO :cleaning
)

IF "%_MONGO_HOME%"=="" (
    ECHO MongoDb install path is NOT defined
    GOTO :cleaning
)

IF "%_MONGO_DATA_PATH%"=="" (
    ECHO MongoDb data path is NOT defined
    GOTO :cleaning
)

IF "%_MONGO_LOG_PATH%"=="" (
    ECHO MongoDb log path is NOT defined
    GOTO :cleaning
)

IF "%_DB_SERVER_PORT%"=="" (
    ECHO Http server version is not defined, using default
    SET _DB_SERVER_PORT=27017
)

IF "%_JAVA_HOME%"=="" (
    ECHO Java install path is NOT defined
    GOTO :cleaning
)

IF "%_NODE_HOME%"=="" (
    ECHO NodeJs install path is NOT defined
    GOTO :cleaning
)

IF "%_AUTH_HOME%"=="" (
    ECHO Auth server install path is NOT defined
    GOTO :cleaning
)

IF "%_AUTH_SERVER_PORT%"=="" (
    ECHO Auth server version is not defined, using default
    SET _AUTH_SERVER_PORT=8180
)

IF "%_SERVER_ADDRESS%"=="" (
    ECHO Backend server host address is not defined
    GOTO :cleaning
)

IF "%_BACKEND_SERVER_PORT%"=="" (
    ECHO Backend server port is not defined
    GOTO :cleaning
)

IF "%_FRONTEND_SERVER_PORT%"=="" (
    ECHO Backend server port is not defined, using default
    SET _FRONTEND_SERVER_PORT=8080
)

IF "%_HTTP_SERVER_VER%"=="" (
    ECHO Http server version is not defined, using default
    SET _HTTP_SERVER_VER=0.11.1
)

SET _SCRIPTS_DIR=%_INSTALL_PATH%\scripts
SET _LOGS_DIR=%_INSTALL_PATH%\logs
SET _CONFIG_PATH=%_INSTALL_PATH%\application.properties
SET _LOG_CONFIG_PATH=%_INSTALL_PATH%\log4j2.properties
SET _BACKEND_PATH=%_INSTALL_PATH%\jars\easy-manage.jar
SET _FRONTEND_PATH=%_INSTALL_PATH%\dist

SET _MONGO_BIN_PATH=%_MONGO_HOME%/bin
SET _AUTH_IMPORT_PATH=%_INSTALL_PATH%\import\realm-export.json
SET _AUTH_HTTP_PORT=8180

: set-timestamp
FOR /f "tokens=2 delims==" %%I IN ('wmic os get localdatetime /format:list') DO SET _DATETIME=%%I
SET _DATETIME=%_DATETIME:~0,8%-%_DATETIME:~8,6%

:::::::::::::::::::::::::::::::::::: START DB SERVER ::::::::::::::::::::::::::::::::::

:check_mongod
ECHO START_1.0 Verify if mongod is already running
FOR /F "tokens=1,2" %%G IN ('tasklist /FI "IMAGENAME eq mongod.exe" /fo table /nh') DO (
    IF %%G NEQ No (
        ECHO Mongodb server is already running!
        GOTO start_auth
    )
)

ECHO ---
ECHO Processes listening on db server port %_DB_SERVER_PORT%:
FOR /F "tokens=5" %%G IN ('netstat -ano ^| findstr ":%_DB_SERVER_PORT%\>"') DO (
	FOR /f "tokens=1 delims=," %%A IN ('tasklist /fi "pid eq %%G" /nh /fo:csv') DO echo %%~A
)

:start_mongod
ECHO START_1.1 Start mongo
powershell -command "Start-Process powershell -ArgumentList '%_MONGO_BIN_PATH%\mongod --dbpath=%_MONGO_DATA_PATH% --logpath=%_MONGO_LOG_PATH%  >> %_LOGS_DIR%\mongod-%_DATETIME%.log 2>&1' -WindowStyle hidden"

ECHO START_1.2 Waiting for the daemon to start...
timeout 15

:::::::::::::::::::::::::::::::::::: START AUTH :::::::::::::::::::::::::::::::::::::

:start_auth
ECHO START_X.1 Verify if auth application is already running
FOR /F "tokens=1,2" %%G IN ('jps') DO (
	IF %%G == jboss-modules.jar (
	    ECHO Auth application is already running!
        GOTO start_backend
    )
)

ECHO -/
ECHO START_X.2.1 Forcibly closing the process which is using the auth server %_AUTH_SERVER_PORT% port
FOR /F "tokens=5" %%G IN ('netstat -ano ^| findstr ":%_AUTH_SERVER_PORT%\>" ^| FIND "LISTENING"') DO (
	FOR /f "tokens=1 delims=," %%A IN ('tasklist /fi "pid eq %%G" /nh /fo:csv') DO echo Stopping process %%~A
	taskkill /f /pid %%G
)

ECHO START_X.2.2 Starting new auth server instance...
powershell -command "Start-Process powershell -ArgumentList 'cd \"%_SCRIPTS_DIR%\"; & .\run-auth.bat %_AUTH_HOME% %_SERVER_ADDRESS% %_AUTH_HTTP_PORT% %_AUTH_IMPORT_PATH% >> %_LOGS_DIR%\auth-%_DATETIME%.log 2>&1' -WindowStyle hidden"

:::::::::::::::::::::::::::::::::::: START BACKEND :::::::::::::::::::::::::::::::::::::

:: TODO - use 'starts with' instead

:start_backend
ECHO START_2.1 Verify if backend application is already running
FOR /F "tokens=1,2" %%G IN ('jps') DO (
	IF %%G == easy-manage.jar (
	    ECHO Backend application is already running!
        GOTO start_frontend
    )
)

ECHO ---
ECHO START_2.2.1 Forcibly closing the process which is using the backend server %_BACKEND_SERVER_PORT% port
FOR /F "tokens=5" %%G IN ('netstat -ano ^| findstr ":%_BACKEND_SERVER_PORT%\>" ^| FIND "LISTENING"') DO (
	FOR /f "tokens=1 delims=," %%A IN ('tasklist /fi "pid eq %%G" /nh /fo:csv') DO echo Stopping process %%~A
    taskkill /f /pid %%G
)

ECHO START_2.2.2 Starting new server instance...
powershell -command "Start-Process powershell -ArgumentList 'cd \"%_SCRIPTS_DIR%\"; & .\run-backend.bat %_BACKEND_PATH% %_CONFIG_PATH% %_LOG_CONFIG_PATH% >> %_LOGS_DIR%\backend-%_DATETIME%.log 2>&1' -WindowStyle hidden"


:::::::::::::::::::::::::::::::::::: START FRONTEND :::::::::::::::::::::::::::::::::::::

:start_frontend

:node_configure
ECHO START_3.0.1 check if http-server is installed
FOR /f "tokens=2" %%G IN ('%_NODE_HOME%\npm list -g --depth=0 2^>^&1 ^| findstr /i "http-server"') DO (
    IF %%G == http-server@%_HTTP_SERVER_VER% (
        ECHO Http server is already installed!
        GOTO http_server_configure
    )
)

:http_server_install
ECHO START_3.0.2 Installing http server
powershell -command %_NODE_HOME%\npm install -g http-server

:http_server_configure
ECHO START_3.1 Verify if frontend application is already running
FOR /F "tokens=1,2" %%G IN ('tasklist /FI "IMAGENAME eq node.exe" /fo table /nh') DO (
    IF %%G NEQ No (
        ECHO Frontend application is already running!
        GOTO cleaning
    )
)

ECHO START_3.2.1 Update backend address in the frontend environment configuration
powershell -command "(gc %_INSTALL_PATH%\dist\static\env.js) -replace 'localhost', '%_SERVER_ADDRESS%' | Out-File %_INSTALL_PATH%\dist\static\env.js"

ECHO ---
ECHO START_3.2.2 Forcibly closing the process which is using the frontend server %_FRONTEND_SERVER_PORT% port
FOR /F "tokens=5" %%G IN ('netstat -ano ^| findstr ":%_FRONTEND_SERVER_PORT%\>" ^| FIND "LISTENING"') DO (
	FOR /f "tokens=1 delims=," %%A IN ('tasklist /fi "pid eq %%G" /nh /fo:csv') DO echo %%~A
	taskkill /f /pid %%G
)

ECHO START_3.2.3 Starting new client instance...
powershell -command "Start-Process powershell -ArgumentList 'http-server %_FRONTEND_PATH% -p %_FRONTEND_SERVER_PORT% >> %_LOGS_DIR%\frontend-%_DATETIME%.log 2>&1' -WindowStyle hidden"

::::::::::::::::::::::::::::::::::: POST PROCESSING :::::::::::::::::::::::::::::::::::::

:cleaning
ECHO ON
