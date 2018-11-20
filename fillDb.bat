@echo off

set MONGOADDR=C:\Dev\mongo\bin\mongo.exe
set MONGOIMPORTADDR=C:\Dev\mongo\bin\mongoimport.exe

"%MONGOADDR%" --eval db.dropDatabase(^)
for /l %%x in (1, 1, 10) do (
   echo Adding element number: %%x
   "%MONGOIMPORTADDR%" -d test -c subUnit --file "%~dp0\src\main\resources\subUnit%%x.json"
   )

