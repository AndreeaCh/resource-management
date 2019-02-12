@echo off

set COMMAND=
if "%1%"=="start" (
    cd %EASYMAN_HOME% & .\start.bat
)
if "%1%"=="stop" (
   cd %EASYMAN_HOME% & .\stop.bat
)

if "%1%"=="import" (
   cd %EASYMAN_HOME% & .\import.bat %MONGO_HOME%\bin %EASYMAN_HOME%\import
)

if "%1%"=="uninstall" (
   cd %EASYMAN_HOME% & .\uninstall.bat
)

if "%1%"=="uninstall-all" (
   cd %EASYMAN_HOME% & .\uninstall.bat all
)

if "%1%"=="help" (
   echo Usage: easyman "[start|stop|import|uninstall|help]"
)

if "%~1%"=="" (
   echo Usage: easyman "[start|stop|import|uninstall|help]"
)

:: TODO : the help menu should be printed for any unrecognized option
