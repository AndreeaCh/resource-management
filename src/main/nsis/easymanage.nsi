# Header section

!include "nsDialogs.nsh"
!include "TextFunc.nsh"
!include "WordFunc.nsh"
!include "EnvVarUpdate.nsh"
!include 'LogicLib.nsh'
!include "psexec.nsh"

;--------------------------------

; Attributes section

Name "Easy Manage"

AllowRootDirInstall true

;TODO Install to $PROGRAMFILES64\EasyManage should be possible when paths containing spaces are handled correctly
InstallDir C:\EasyManage

Var _SCRIPTS_DIR
Var _IMPORT_DIR
Var _LOGS_DIR
Var _BIN_DIR
Var _BACKEND_DIR
Var _FRONTEND_DIR

; chocolatey path constants
Var _CHOCO_VERSION

; java path constants
Var _JAVA_INSTALL_OPTION
Var _JAVA_VERSION

; db path constants
Var _MONGO_SERVER_PATH
Var _MONGO_DATA_PATH
Var _MONGO_LOG_PATH
Var _DB_VERSION
Var _DB_INSTALL_OPTION
Var _DB_INSTALL_PARAMS

; node path constants
Var _NODE_VERSION

; dynamic variables
Var _SERVER_ADDRESS

; show installation details for debug purposes
ShowInstDetails show

; Registry key to check for directory (so if you install again, it will overwrite the old one automatically)
InstallDirRegKey HKLM "Software\NSIS_EasyManage" "Install_Dir"

; Request application privileges for Windows
RequestExecutionLevel admin

;--------------------------------

# Pages

Page components
Page directory
Page Custom serverAddressPage serverAddressPageLeave
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

;--------------------------------

Section "-Meta setup"

   SectionIn RO

   StrCpy $_SCRIPTS_DIR $INSTDIR\scripts
   StrCpy $_IMPORT_DIR $INSTDIR\import
   StrCpy $_LOGS_DIR $INSTDIR\logs
   StrCpy $_BIN_DIR $INSTDIR\bin
   StrCpy $_BACKEND_DIR $INSTDIR\jars
   StrCpy $_FRONTEND_DIR $INSTDIR\dist

   StrCpy $_CHOCO_VERSION '0.10.11'

   StrCpy $_JAVA_INSTALL_OPTION zulu
   StrCpy $_JAVA_VERSION '11.29.3'

   StrCpy $_MONGO_SERVER_PATH 'C:\Progra~1\MongoDB\Server\4.0'
   StrCpy $_MONGO_DATA_PATH 'C:\mongodb\data\db'
   StrCpy $_MONGO_LOG_PATH 'C:\mongodb\log'

   StrCpy $_DB_INSTALL_OPTION 'mongodb'
   StrCpy $_DB_VERSION '4.0.6'
   StrCpy $_DB_INSTALL_PARAMS '/dataPath:$_MONGO_DATA_PATH /logPath:$_MONGO_LOG_PATH'

   StrCpy $_NODE_VERSION '10.15.0'

   SetRegView 64

   ; Write the installation path into the registry
   WriteRegStr HKLM SOFTWARE\NSIS_EasyManage "Install_Dir" "$INSTDIR"

   ; Write the uninstall keys for Windows
   WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\EasyManage" "DisplayName" "NSIS EasyManage"
   WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\EasyManage" "UninstallString" '"$INSTDIR\uninstall.exe"'
   WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\EasyManage" "NoModify" 1
   WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\EasyManage" "NoRepair" 1
   WriteUninstaller "$INSTDIR\uninstall.exe"

   ; extract setup script
   SetOutPath $INSTDIR

   File setup/setup.bat
   File /oname=install.md setup/install_instructions.md

   ; extract install script files
   SetOutPath $_SCRIPTS_DIR

   File setup/install_chocolatey.bat
   File setup/install_db.bat
   File setup/install_java.bat
   File setup/install_node.bat
   File setup/uninstall.bat
   File setup/uninstall_tools.bat
   File setup/set_execution_policy.bat
   File /r *.cmd
   File /r *.ps1

   CreateDirectory $_LOGS_DIR

SectionEnd

Section "!Chocolatey (required)"

   SetOutPath $INSTDIR

   nsExec::ExecToStack 'powershell -inputformat none -command choco -V'
   Pop $0
   Pop $1

   Var /Global _INSTALLED_CHOCO_VERSION
   StrCpy $_INSTALLED_CHOCO_VERSION "$1"

   DetailPrint "Verify if 'chocolatey' is installed. Required version is $\"$_CHOCO_VERSION$\""

   ${VersionCompare} "$_INSTALLED_CHOCO_VERSION" "$_CHOCO_VERSION" $R0

   ${If} $R0 == 0
       DetailPrint "Chocolatey is installed ( v. $_INSTALLED_CHOCO_VERSION )"
   ${Else}
       DetailPrint "Choco is not installed : Output was $\"$_INSTALLED_CHOCO_VERSION$\""

       SetOutPath $_SCRIPTS_DIR\chocolatey

       ; todo: hide windows
       ;nsExec::ExecToLog 'powershell -inputformat none -ExecutionPolicy Bypass -command "Invoke-Item -Path $_SCRIPTS_DIR\chocolatey\installChocolatey.cmd"'
       ExecWait '"$_SCRIPTS_DIR\set_execution_policy.bat" Unrestricted' $0
       ExecWait '"$_SCRIPTS_DIR\chocolatey\installChocolatey.cmd"'
       ExecWait '"$_SCRIPTS_DIR\set_execution_policy.bat" Restricted' $0
   ${EndIf}

   ;ExecWait '"$INSTDIR\setup.bat" prereq /S' $0
   DetailPrint "Install prerequisites returned $0"

SectionEnd

Section "Mongodb (required)"

   SetOutPath $INSTDIR

   ;ExecWait '"$INSTDIR\setup.bat" db /S' $0
   nsExec::ExecToLog 'powershell -inputformat none -ExecutionPolicy Bypass -command choco install $_DB_INSTALL_OPTION -y --version $_DB_VERSION --params="$_DB_INSTALL_PARAMS"'
   Pop $0
   DetailPrint "Install mongodb returned $0"

   ; set environment variables
   DetailPrint "Set MONGO_HOME environment variable"
   ${EnvVarUpdate} $0 "MONGO_HOME" "A" "HKLM" "$_MONGO_SERVER_PATH"

   DetailPrint "Add mongo bin to PATH environment variable"
   ${EnvVarUpdate} $0 "PATH" "A" "HKLM" "$_MONGO_SERVER_PATH\bin"

SectionEnd

Section "Java (required)"

   SetOutPath $INSTDIR

   ; ExecWait '"$INSTDIR\setup.bat" java' $0
   nsExec::ExecToLog 'choco install $_JAVA_INSTALL_OPTION -y --version $_JAVA_VERSION'
   Pop $0

   DetailPrint "Install java returned $0"

SectionEnd

Section "NodeJs (required)"

   SetOutPath $INSTDIR

   ; ExecWait '"$INSTDIR\setup.bat" node' $0
   nsExec::ExecToLog 'choco install nodejs -y --version $_NODE_VERSION'
   Pop $0

   DetailPrint "Install nodejs returned $0"

SectionEnd

Section "!EasyManage (required)"

   ; extract run script files
   SetOutPath $_SCRIPTS_DIR

   File run/start.bat
   File run/stop.bat
   File /oname=run-backend.bat run/deploy.bat
   File /oname=import.bat run/fillDb.bat

   ; copy import data
   SetOutPath $_IMPORT_DIR
   File fillDb\*.json

   ; copy backend binary
   SetOutPath $_BACKEND_DIR
   File /oname=easy-manage.jar bin/resource-management.jar

   ; copy frontend structure
   SetOutPath $_FRONTEND_DIR
   File /r dist\*.*

   ; extract binary and configuration files
   SetOutPath $INSTDIR

   File /oname=README.md run/run_instructions.md
   SetFileAttributes README.md READONLY

   File /oname=application.properties run/production.application.properties

   SetOutPath $_BIN_DIR

   File /oname=easymanage.bat run/easymanage.bat
   SetFileAttributes easymanage.bat READONLY

   ; write config file
   DetailPrint "Writing config file in user profile folder ($PROFILE)"

   FileOpen $0 "$PROFILE\easymanage.conf" w
   FileClose $0

   ${ConfigWrite} "$PROFILE\easymanage.conf" "INSTALL_PATH=" "$INSTDIR" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "SERVER_ADDRESS=" "$_SERVER_ADDRESS" $R0

   ; set environment variables
   DetailPrint "Set EASYMAN_HOME environment variable"
   ${EnvVarUpdate} $0 "EASYMAN_HOME" "A" "HKLM" "$INSTDIR"

   DetailPrint "Add easymanage to PATH environment variable"
   ${EnvVarUpdate} $0 "PATH" "A" "HKLM" "$_BIN_DIR"

SectionEnd

; Optional section (can be disabled by the user)
Section /o "Start Menu Shortcuts"

   CreateDirectory "$SMPROGRAMS\EasyManage"
   CreateShortcut "$SMPROGRAMS\EasyManage\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
   CreateShortcut "$SMPROGRAMS\EasyManage\EasyManage (MakeNSISW).lnk" "$_BIN_DIR\easymanage.bat" "" "$_BIN_DIR\easymanage.bat" 0

SectionEnd

Section /o "Desktop Shortcut" SectionX
    SetShellVarContext current

    SetOutPath $DESKTOP
    CreateShortCut "Start EasyManage.lnk" "$INSTDIR\bin\easymanage.bat" \
        "start" "" "" SW_SHOWNORMAL \
        "" "Start EasyManage"

    CreateShortCut "Stop EasyManage.lnk" "$INSTDIR\bin\easymanage.bat" \
            "stop" "" "" SW_SHOWNORMAL \
            "" "Stop EasyManage"
SectionEnd

; contents should be moved to a separate function
Section "-Cleanup"

    ; Remove files and uninstaller
    Delete $_SCRIPTS_DIR\install_chocolatey.bat
    Delete $_SCRIPTS_DIR\install_chocolatey.bat
    Delete $_SCRIPTS_DIR\install_db.bat
    Delete $_SCRIPTS_DIR\install_java.bat
    Delete $_SCRIPTS_DIR\install_node.bat
    Delete $INSTDIR\setup.bat

SectionEnd

;--------------------------------

# Uninstaller

Section "Uninstall"

   ; TODO : do the actual uninstall steps
   ExecWait '"$INSTDIR\bin\easymanage.bat" uninstall' $0
   DetailPrint "Uninstall script returned $0"

    ; unset environment variables
   DetailPrint "Unset EASYMAN_HOME environment variable"
   ${un.EnvVarUpdate} $0 "EASYMAN_HOME" "R" "HKLM" "$INSTDIR"

   DetailPrint "Remove easymanage bin dir from PATH environment variable"
   ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" "$_BIN_DIR"

   DetailPrint "Unset MONGO_HOME environment variable"
   ${un.EnvVarUpdate} $0 "MONGO_HOME" "R" "HKLM" "$_MONGO_SERVER_PATH"

   DetailPrint "Remove mongo bin dir from PATH environment variable"
   ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" "$_MONGO_SERVER_PATH\bin"

   SetRegView 64

   ; Remove registry keys
   DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\EasyManage"
   DeleteRegKey HKLM SOFTWARE\NSIS_EasyManage

   ; Remove files

   ;RMDir /r $_SCRIPTS_DIR
   ;RMDir /r $_IMPORT_DIR
   ;RMDir /r $_BACKEND_DIR
   ;RMDir /r $_FRONTEND_DIR

   ;Delete "$_LOGS_DIR\*.*"
   ;Delete $_LOGS_DIR

   ;Delete $_BIN_DIR\easymanage.bat
   ;Delete /r $_BIN_DIR

   ; Remove shortcuts, if any
   Delete "$SMPROGRAMS\EasyManage\*.*"
   RMDir "$SMPROGRAMS\EasyManage"
   Delete "$DESKTOP\*EasyManage.lnk"

   ;Remove 'chocolatey'
   DetailPrint "Unset ChocolateyInstall environment variable $\"$APPDATA\chocolatey$\""
   ${un.EnvVarUpdate} $0 "ChocolateyInstall" "R" "HKLM" "$APPDATA\chocolatey"
   ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" "$APPDATA\chocolatey\bin"

   SetShellVarContext all
   DetailPrint "Deleting $APPDATA\chocolatey"
   RMDir /r "$APPDATA\chocolatey"

   ; Remove uninstall and install folder
   ;Delete $INSTDIR\uninstall.exe
   ;RMDir "$INSTDIR"

SectionEnd

;--------------------------------

# Functions

;Function .onInit
;  MessageBox MB_YESNO "This will install Easy Manage on your computer. Do you wish to continue?" IDYES NoAbort
;    Abort ; causes the installer to quit
;  NoAbort:
;FunctionEnd

Function serverAddressPage
    Var /Global ServerAddressText
    nsDialogs::Create /NOUNLOAD 1018
    Pop $0

    ${NSD_CreateLabel} 0 0 100% 12u "Server address (eg. cjweb)"
    Pop $0

    ${NSD_CreateText} 10% 20u 80% 12u "localhost"
    Pop $ServerAddressText

    nsDialogs::Show
FunctionEnd

Function serverAddressPageLeave

	${NSD_GetText} $ServerAddressText $0
	StrCpy $_SERVER_ADDRESS $0
	;MessageBox MB_OK "The server will be reachable under the domain name:$\n$\n$_SERVER_ADDRESS"

FunctionEnd

Function .onInstFailed
    MessageBox MB_OK "Installation failed. Check the logs"
FunctionEnd
