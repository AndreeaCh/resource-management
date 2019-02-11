@echo off

set COMMAND=
if "%1%"=="start" (
   set COMMAND=start.bat
)
if "%1%"=="stop" (
   set COMMAND=stop.bat
)

if "%1%"=="import" (
   set COMMAND=import.bat
)

if "%1%"=="uninstall" (
   set COMMAND=uninstall.bat
)

if "%1%"=="uninstall-all" (
   set COMMAND=uninstall.bat all
)


if "%COMMAND%"=="" (
   echo Usage: easyman "[start|stop|import|uninstall|help]"
) else (
   cd %EASYMAN_HOME% & .\%COMMAND%
)
