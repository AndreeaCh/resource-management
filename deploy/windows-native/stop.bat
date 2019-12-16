:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch stop.bat - Stops ISU Resource Management backend and frontend services
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

ECHO STOP_1 Stopping frontend server ...
TASKKILL /IM node.exe /F

ECHO STOP_2 Stopping backend server and auth server ...
TASKKILL /IM java.exe /F

:cleaning
ECHO ON