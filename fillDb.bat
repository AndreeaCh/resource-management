@echo off
if "%~1%"=="" (
   echo Please specify the path to the /bin folder of your mongoDB installation
) else (

SET _IMPORT_PATH=%2
IF "%_IMPORT_PATH%"=="" SET _IMPORT_PATH=%~dp0\src\main\resources

set MONGOADDR=%~1%\mongo.exe
set MONGOIMPORTADDR=%~1%\mongoimport.exe

"%MONGOADDR%" --eval db.dropDatabase(^)
for /l %%x in (0, 1, 10) do (
   echo Adding element number: %%x
   "%MONGOIMPORTADDR%" -d test -c subUnit --file "%_IMPORT_PATH%\subUnit%%x.json"
   )
)

for /l %%x in (0, 1, 2) do (
   echo Adding service number: %%x
   "%MONGOIMPORTADDR%" -d test -c service --file "%_IMPORT_PATH%\service%%x.json"
   )
)