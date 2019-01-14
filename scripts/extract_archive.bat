:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Extracts archive to target directory
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

SET _ARCHIVE_PATH=%1
SET _INSTALL_DIR=%2

IF "%_ARCHIVE_PATH%"=="" (
    ECHO Archive path is NOT defined
    GOTO :cleaning
)

IF "%_INSTALL_DIR%"=="" (
    ECHO Install path is NOT defined
    GOTO :cleaning
)

:::::::::::::::::::::::::::: EXTRACT ARCHIVE TO INSTALL FOLDER ::::::::::::::::::::::::::::::

:create_folders
ECHO EXTRACT_0 Creating install folder
mkdir "%_INSTALL_DIR%" 2>nul
IF NOT EXIST "%_INSTALL_DIR%\*" (
    ECHO Failed to create directory "%_INSTALL_DIR%"
    GOTO :cleaning
)

:extract_distributable
ECHO EXTRACT_1 Extracting archive...
tar -xf %_ARCHIVE_PATH% -C %_INSTALL_DIR% --strip-components=1

:::::::::::::::::::::::::::::::::::: POST PROCESSING ::::::::::::::::::::::::::::::::::::::::

:cleaning
ECHO ON