@echo off
if "%~1%"=="" (
   echo Please specify the path to the /bin folder of your mongoDB installation
) else (

set MONGOADDR=%~1%\mongo.exe
set MONGOIMPORTADDR=%~1%\mongoimport.exe

"%MONGOADDR%" --eval db.dropDatabase(^)
for /l %%x in (1, 1, 5) do (
   echo Adding element number: %%x
   "%MONGOIMPORTADDR%" -d test -c subUnit --file "%~dp0\src\main\resources\subUnit%%x.json"
   )
)
