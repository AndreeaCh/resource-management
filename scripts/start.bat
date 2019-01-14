:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch start.bat - Starts the ISU Resource Management application bundle
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

SET _DEMO_MODE=%1
SET _SCRIPTS_DIR=.\scripts
SET _IMPORT_DIR=.\import

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

:: mongodb constants - modify this as needed
SET _MONGO_BIN_PATH=C:\Progra~1\MongoDB\Server\4.0\bin

: set-timestamp
FOR /f "tokens=2 delims==" %%I IN ('wmic os get localdatetime /format:list') DO SET _DATETIME=%%I
SET _DATETIME=%_DATETIME:~0,8%-%_DATETIME:~8,6%

:::::::::::::::::::::::::::::::::::::: START DB SERVER ::::::::::::::::::::::::::::::::::::::

:check_mongod
ECHO START_1.0 Verify if mongod has already started
FOR /F "tokens=1,2" %%G IN ('tasklist /FI "IMAGENAME eq mongod.exe" /fo table /nh') DO (
    IF %%H NEQ No (
        GOTO fill_db
    )
)

:start_mongod
ECHO START_1.1 Start mongo
powershell -command "Start-Process powershell -ArgumentList 'mongod --config %_MONGO_BIN_PATH%\mongod.cfg  >> logs\mongod-%_DATETIME%.log 2>&1' -WindowStyle hidden"
::START /B %_MONGO_BIN_PATH%\mongod.exe --config %_MONGO_BIN_PATH%\mongod.cfg

ECHO Waiting for the daemon to start...
timeout 15

:fill_db
IF NOT "%_DEMO_MODE%" == "demo" (
    GOTO start_backend
)

ECHO START_1.2 Fill database with predefined data
%_IMPORT_DIR%\fillDb.bat %_MONGO_BIN_PATH%

:::::::::::::::::::::::::::::::::::::: START BACKEND ::::::::::::::::::::::::::::::::::::::

:start_backend
ECHO START_2.1 Verify if backend application has already started
FOR /F "tokens=1,2" %%G IN ('tasklist /FI "IMAGENAME eq java.exe" /fo table /nh') DO (
    IF %%H NEQ No (
        GOTO start_frontend
    )
)

ECHO START_2.2 Starting new server instance...
powershell -command "Start-Process powershell -ArgumentList 'cd \"%CD%\"; & .\run-backend.bat >> logs\backend-%_DATETIME%.log 2>&1' -WindowStyle hidden"


:::::::::::::::::::::::::::::::::::::: START FRONTEND ::::::::::::::::::::::::::::::::::::::

:start_frontend
ECHO START_3.1 Verify if frontend application has already started
FOR /F "tokens=1,2" %%G IN ('tasklist /FI "IMAGENAME eq node.exe" /fo table /nh') DO (
    IF %%H NEQ No (
        GOTO cleaning
    )
)

ECHO START_3.2 Starting new client instance...
powershell -command "Start-Process powershell -ArgumentList 'cd \"%CD%\"; & http-server dist -p 8080 >> logs\frontend-%_DATETIME%.log 2>&1' -WindowStyle hidden"

:::::::::::::::::::::::::::::::::::: POST PROCESSING ::::::::::::::::::::::::::::::::::::::::

:cleaning

ECHO ON