:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Batch install.bat - Installs all the programs
:: needed for the installation of Resource Management application
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

set _POLICY=%~1
if "%1%"=="" (
    SET _POLICY=Unrestricted
)

ECHO OFF
ECHO POLICY Set execution policy

:powershell_activation
ECHO INSTALL_0.1 Activate powershell...
FOR /f %%G IN ('powershell Get-ExecutionPolicy') DO (IF "%%G" == "%_POLICY%" (GOTO cleaning))
powershell Set-ExecutionPolicy %_POLICY%

:cleaning
ECHO POLICY Finished
ECHO ON