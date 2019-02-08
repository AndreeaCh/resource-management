ECHO OFF

if "%~1%"=="" (
   ECHO Please specify the path to the /bin folder of your mongoDB installation
   GOTO :cleaning
)

SET _IMPORT_PATH=%2
IF "%_IMPORT_PATH%"=="" SET _IMPORT_PATH=%~dp0\src\main\resources

set MONGOADDR=%~1%\mongo.exe
set MONGOIMPORTADDR=%~1%\mongoimport.exe

"%MONGOADDR%" --eval db.dropDatabase(^)
for /l %%x in (0, 1, 10) do (
   ECHO Adding subunit number: %%x
   "%MONGOIMPORTADDR%" -d test -c subUnit --file "%_IMPORT_PATH%\subUnit%%x.json"
   )
)

for /l %%x in (0, 1, 2) do (
   ECHO Adding service number: %%x
   "%MONGOIMPORTADDR%" -d test -c service --file "%_IMPORT_PATH%\service%%x.json"
   )
)

for /l %%x in (0, 1, 0) do (
   ECHO Adding function number: %%x
   "%MONGOIMPORTADDR%" -d test -c functions --file "%_IMPORT_PATH%\function%%x.json"
   )
)

for /l %%x in (0, 1, 0) do (
   ECHO Adding vehicle number: %%x
   "%MONGOIMPORTADDR%" -d test -c vehicleTypes --file "%_IMPORT_PATH%\vehicle%%x.json"
   )
)

:cleaning
ECHO ON