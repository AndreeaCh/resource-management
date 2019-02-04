:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch start.bat - Stops ISU Resource Management backend and frontend services
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

ECHO OFF

:: TODO: processes neeed to be identified precisely, otherwise we end up stopping other processes

ECHO STOP_1 Stop frontend server
TASKKILL /IM node.exe /F

ECHO STOP_2 Stop backend server
TASKKILL /IM java.exe /F

:cleaning

ECHO ON
PAUSE