@echo off
if "%~1%"=="" (
   echo Please specify the path to the /bin folder of your mongoDB installation
) else (

set MONGOADDR=%~1%\mongo.exe
set MONGOIMPORTADDR=%~1%\mongoimport.exe

"%MONGOADDR%" --eval db.dropDatabase(^)
for /l %%x in (0, 1, 10) do (
   echo Adding subunit number: %%x
   "%MONGOIMPORTADDR%" -d test -c subUnit --file "%~dp0\src\main\resources\subUnit%%x.json"
   )
)

for /l %%x in (0, 1, 2) do (
   echo Adding service number: %%x
   "%MONGOIMPORTADDR%" -d test -c service --file "%~dp0\src\main\resources\service%%x.json"
   )
)

for /l %%x in (0, 1, 0) do (
   echo Adding function number: %%x
   "%MONGOIMPORTADDR%" -d test -c functions --file "%~dp0\src\main\resources\function%%x.json"
   )
)

for /l %%x in (0, 1, 0) do (
   echo Adding vehicle number: %%x
   "%MONGOIMPORTADDR%" -d test -c vehicleTypes --file "%~dp0\src\main\resources\vehicle%%x.json"
   )
)