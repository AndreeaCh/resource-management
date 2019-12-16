::@echo off

:::::::::::::::::::::
:: Add admin user  ::
:::::::::::::::::::::

SET WDIR=%~dp0

SET _KEYCLOAK_HOME=%~1
SET _SERVER_ADDRESS=%~2
SET _SERVER_PORT=%~3
SET _KEYCLOAK_IMPORT_PATH=%~4

SET _KEYCLOAK_BIN_PATH=%_KEYCLOAK_HOME%\bin

SET _AUTH_SERVER_OPTIONS=-Djboss.bind.address="%_SERVER_ADDRESS%"
SET _AUTH_SERVER_OPTIONS=%_AUTH_SERVER_OPTIONS% -Djboss.http.port="%_SERVER_PORT%"

:: SET _AUTH_SERVER_OPTIONS=%_AUTH_SERVER_OPTIONS% -Dkeycloak.import="%_KEYCLOAK_IMPORT_PATH%"
:: SET _AUTH_SERVER_OPTIONS=%_AUTH_SERVER_OPTIONS% -Dkeycloak.migration.action=import
:: SET _AUTH_SERVER_OPTIONS=%_AUTH_SERVER_OPTIONS% -Dkeycloak.migration.provider=singleFile
:: SET _AUTH_SERVER_OPTIONS=%_AUTH_SERVER_OPTIONS% -Dkeycloak.migration.file="%_KEYCLOAK_IMPORT_PATH%"
:: SET _AUTH_SERVER_OPTIONS=%_AUTH_SERVER_OPTIONS% -Dkeycloak.migration.strategy=OVERWRITE_EXISTING

ECHO START_1.1 Start server and import realm

echo keycloak server options %_AUTH_SERVER_OPTIONS%

CD %_KEYCLOAK_BIN_PATH%
.\standalone.bat -Djboss.bind.address=%_SERVER_ADDRESS% -Djboss.http.port=%_SERVER_PORT% ^
-Dkeycloak.import=%_KEYCLOAK_IMPORT_PATH%

CD %WDIR%

:: run_auth.bat C:\Keycloak admin 1337Hex localhost 8081 C:\Keycloak\realm-export.json