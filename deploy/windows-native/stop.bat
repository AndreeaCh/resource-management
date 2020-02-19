:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch stop.bat - Stops ISU Resource Management backend and frontend services
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

ECHO Reading configuration '%UserProfile%\easymanage.conf'
For /F "tokens=1* delims==" %%A IN (%UserProfile%\easymanage.conf) DO (

    IF "%%A"=="SERVER_PORT" set _BACKEND_SERVER_PORT=%%B

    IF "%%A"=="FRONTEND_SERVER_PORT" set _FRONTEND_SERVER_PORT=%%B

    IF "%%A"=="AUTH_SERVER_PORT" set _AUTH_SERVER_PORT=%%B

    IF "%%A"=="DB_SERVER_PORT" set _DB_SERVER_PORT=%%B
)

ECHO Configured BACKEND_SERVER_PORT is '%_BACKEND_SERVER_PORT%'
ECHO Configured FRONTEND_SERVER_PORT is '%_FRONTEND_SERVER_PORT%'
ECHO Configured AUTH_SERVER_PORT is '%_AUTH_SERVER_PORT%'
ECHO Configured DB_SERVER_PORT is '%_DB_SERVER_PORT%'

::::::::::::::::::::::::::::::::: PATH CONSTANTS ::::::::::::::::::::::::::::::::::::::

: set-path-constants

IF "%_BACKEND_SERVER_PORT%"=="" (
    ECHO Backend server port is not defined
    GOTO :cleaning
)

IF "%_FRONTEND_SERVER_PORT%"=="" (
    ECHO Backend server port is not defined, using default
    SET _FRONTEND_SERVER_PORT=8080
)

IF "%_AUTH_SERVER_PORT%"=="" (
    ECHO Http server version is not defined, using default
    SET _AUTH_SERVER_PORT=8180
)

IF "%_DB_SERVER_PORT%"=="" (
    ECHO Http server version is not defined, using default
    SET _DB_SERVER_PORT=27017
)

ECHO ---
ECHO STOP_1/6 Stopping processes listening on frontend server port %_FRONTEND_SERVER_PORT%:
FOR /F "tokens=5" %%G IN ('netstat -ano ^| findstr ":%_FRONTEND_SERVER_PORT%\>" ^| FIND "LISTENING"') DO (
	FOR /f "tokens=1 delims=," %%A IN ('tasklist /fi "pid eq %%G" /nh /fo:csv') DO echo Stopping process %%~A
	taskkill /PID %%G /F
)

ECHO ---
ECHO STOP_2/6 Stopping processes listening on backend server port %_BACKEND_SERVER_PORT%:
FOR /F "tokens=5" %%G IN ('netstat -ano ^| findstr ":%_BACKEND_SERVER_PORT%\>" ^| FIND "LISTENING"') DO (
	FOR /f "tokens=1 delims=," %%A IN ('tasklist /fi "pid eq %%G" /nh /fo:csv') DO echo Stopping process %%~A
	taskkill /PID %%G /F
)

ECHO ---
ECHO STOP_3/6 Stopping Processes listening on auth server port %_AUTH_SERVER_PORT%:
FOR /F "tokens=5" %%G IN ('netstat -ano ^| findstr ":%_AUTH_SERVER_PORT%\>" ^| FIND "LISTENING"') DO (
	FOR /f "tokens=1 delims=," %%A IN ('tasklist /fi "pid eq %%G" /nh /fo:csv') DO echo Stopping process %%~A
	taskkill /PID %%G /F
)

ECHO ---
ECHO STOP_4/6 Stopping processes listening on db server port %_DB_SERVER_PORT%:
FOR /F "tokens=5" %%G IN ('netstat -ano ^| findstr ":%_DB_SERVER_PORT%\>" ^| FIND "LISTENING"') DO (
    FOR /f "tokens=1 delims=," %%A IN ('tasklist /fi "pid eq %%G" /nh /fo:csv') DO echo Stopping process %%~A
    taskkill /PID %%G /F
)

ECHO ---
ECHO STOP_5/6 Clearing all remaining node processes ...
TASKKILL /IM node.exe /F

ECHO ---
ECHO STOP_6/6 Clearing all remaining java processes ...
TASKKILL /IM java.exe /F

ECHO ---
:cleaning
ECHO ON