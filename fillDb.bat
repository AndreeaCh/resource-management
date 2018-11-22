@echo off

set MONGOADDR=C:\Program Files\MongoDB\Server\3.0\bin\mongo.exe
set MONGOIMPORTADDR=C:\Program Files\MongoDB\Server\3.0\bin\mongoimport.exe

"%MONGOADDR%" --eval db.dropDatabase(^)
for /l %%x in (1, 1, 10) do (
   echo Adding element number: %%x
   "%MONGOIMPORTADDR%" -d test -c subUnit --file "%~dp0\src\main\resources\subUnit%%x.json"
   )

