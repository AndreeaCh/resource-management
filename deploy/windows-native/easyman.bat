@echo off

set _COMMAND=
if "%1%"=="start" (
    SET _COMMAND=start.bat
)
if "%1%"=="stop" (
    SET _COMMAND=stop.bat
)

if "%1%"=="import" (
    SET _COMMAND=import.bat %MONGO_HOME%\bin %EASYMAN_HOME%\import
)

if "%1%"=="uninstall" (
    SET _COMMAND=uninstall.bat
)

if "%1%"=="uninstall-all" (
    SET _COMMAND=uninstall.bat all
)


if "%_COMMAND%"=="" (
    echo Usage: easyman "[option]"
    echo Options:
    echo    start                 - start application( frontend, backend, db)
    echo    stop                  - stops application( frontend, backend)
    echo    import                - imports initial data into the db server
    echo    uninstall             - uninstalls application
    echo    uninstall-all         - uninstalls application and its dependencies
    echo    help                  - show help
) else  (
    cd %EASYMAN_HOME% & .\%_COMMAND%
)

:: TODO : the help menu should be printed for any unrecognized option
