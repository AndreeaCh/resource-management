# Header section

!include "nsDialogs.nsh"
!include "TextFunc.nsh"
!include "WordFunc.nsh"
!include "EnvVarUpdate.nsh"
!include 'LogicLib.nsh'
!include "psexec.nsh"

; TODO : supply the plugin path through a makensis argument
; !addplugindir /x86-ansi "/home/marius/workspace/isu/resource-management/src/main/nsis/Plugins/x86-ansi"
;--------------------------------

; Attributes section

Name "Easy Manage"

AllowRootDirInstall true

;TODO Install to $PROGRAMFILES64\EasyManage should be possible when paths containing spaces are handled correctly
InstallDir C:\EasyManage

Var _INSTALL_VERSION
Var _SCRIPTS_DIR
Var _IMPORT_DIR
Var _LOGS_DIR
Var _BIN_DIR
Var _BACKEND_DIR
Var _FRONTEND_DIR
Var _HTTP_SERVER_VERSION

; application constants
Var _BACKEND_PORT
Var _BACKEND_LOG_FILE
Var _BACKEND_LOG_LEVEL
Var _BACKEND_RESOURCE_STATUS_FILE

; java constants
Var _JAVA_INSTALL_KIT
Var _JAVA_INSTALL_OPTION
Var _JAVA_INSTALL_PATH
Var _JAVA_VERSION

; db constants
Var _MONGO_INSTALL_KIT
Var _MONGO_SERVER_PATH
Var _MONGO_DATA_PATH
Var _MONGO_LOG_PATH
Var _DB_VERSION
Var _DB_INSTALL_OPTION

; auth constants
Var _AUTH_INSTALL_OPTION
Var _AUTH_INSTALL_PATH
Var _AUTH_VERSION
Var _AUTH_SERVER_PORT

; node constants
Var _NODE_INSTALL_KIT
Var _NODE_INSTALL_OPTION
Var _NODE_INSTALL_PATH
Var _NODE_VERSION

; dynamic variables
Var _SERVER_ADDRESS
Var _SERVER_PORT

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

   StrCpy $_INSTALL_VERSION '0.0.3-SNAPSHOT'
   StrCpy $_HTTP_SERVER_VERSION '0.11.1'

   StrCpy $_SCRIPTS_DIR $INSTDIR\scripts
   StrCpy $_IMPORT_DIR $INSTDIR\import
   StrCpy $_LOGS_DIR $INSTDIR\logs
   StrCpy $_BIN_DIR $INSTDIR\bin

   StrCpy $_BACKEND_DIR $INSTDIR\jars
   StrCpy $_BACKEND_PORT "8345"
   StrCpy $_BACKEND_LOG_FILE "application.log"
   StrCpy $_BACKEND_LOG_LEVEL "DEBUG"
   StrCpy $_BACKEND_RESOURCE_STATUS_FILE "resource-status-history.txt"

   StrCpy $_FRONTEND_DIR $INSTDIR\dist

   StrCpy $_JAVA_INSTALL_OPTION zulu
   StrCpy $_JAVA_INSTALL_PATH 'C:\Progra~1\Zulu\zulu'
   StrCpy $_JAVA_VERSION '11.37.17'
   StrCpy $_JAVA_INSTALL_KIT 'zulu11.37.17-ca-jdk11.0.6-win_x64.msi'

   StrCpy $_MONGO_SERVER_PATH 'C:\Progra~1\MongoDB\Server\4.0'
   StrCpy $_MONGO_DATA_PATH 'C:\mongodb\data\db'
   StrCpy $_MONGO_LOG_PATH 'C:\mongodb\log'
   StrCpy $_MONGO_INSTALL_KIT 'mongodb-win32-x86_64-2008plus-ssl-4.0.16-signed.msi'

   StrCpy $_DB_INSTALL_OPTION mongodb
   StrCpy $_DB_VERSION '4.0.16'

   StrCpy $_AUTH_INSTALL_OPTION keycloak
   StrCpy $_AUTH_INSTALL_PATH 'C:\Keycloak'
   StrCpy $_AUTH_SERVER_PORT "8180"
   StrCpy $_AUTH_VERSION "8.0.8"

   StrCpy $_NODE_INSTALL_OPTION nodejs
   StrCpy $_NODE_INSTALL_PATH 'C:\Progra~1\nodejs'
   StrCpy $_NODE_VERSION '10.15.0'
   StrCpy $_NODE_INSTALL_KIT 'node-v10.15.0-x64.msi'

   StrCpy $_SERVER_PORT "8345"

   ; save install constants to ini file to be used at uninstall
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "BIN_PATH" $_BIN_DIR
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "SCRIPTS_PATH" $_SCRIPTS_DIR
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "IMPORT_PATH" $_IMPORT_DIR
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "LOGS_PATH" $_LOGS_DIR
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "BACKEND_PATH" $_BACKEND_DIR
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "FRONTEND_PATH" $_FRONTEND_DIR

   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "JAVA_INSTALL_PATH" $_JAVA_INSTALL_PATH
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "NODE_INSTALL_PATH" $_NODE_INSTALL_PATH
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "AUTH_INSTALL_PATH" $_AUTH_INSTALL_PATH
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "MONGO_SERVER_PATH" $_MONGO_SERVER_PATH
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "MONGO_DATA_PATH" $_MONGO_DATA_PATH
   WriteINIStr "$PROFILE\\easymanage.ini" "paths" "MONGO_LOG_PATH" $_MONGO_LOG_PATH

   WriteINIStr "$PROFILE\\easymanage.ini" "dependencies" "JAVA_INSTALL_OPTION" $_JAVA_INSTALL_OPTION
   WriteINIStr "$PROFILE\\easymanage.ini" "dependencies" "DB_INSTALL_OPTION" $_DB_INSTALL_OPTION
   WriteINIStr "$PROFILE\\easymanage.ini" "dependencies" "NODE_INSTALL_OPTION" $_NODE_INSTALL_OPTION
   WriteINIStr "$PROFILE\\easymanage.ini" "dependencies" "AUTH_INSTALL_OPTION" $_AUTH_INSTALL_OPTION


   SetRegView 64

   ; Write the installation path into the registry
   WriteRegStr HKLM SOFTWARE\NSIS_EasyManage "Install_Dir" "$INSTDIR"

   ; Write the uninstall keys for Windows
   WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\EasyManage" "DisplayName" "NSIS EasyManage"
   WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\EasyManage" "UninstallString" '"$INSTDIR\uninstall.exe"'
   WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\EasyManage" "NoModify" 1
   WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\EasyManage" "NoRepair" 1
   WriteUninstaller "$INSTDIR\uninstall.exe"

   ; copy installer kits
   SetOutPath $TEMP
   File msi\*.msi

   ; extract setup script
   SetOutPath $INSTDIR

   File setup\setup.bat
   File /oname=install.md setup\install_instructions.md

   ; extract install script files
   SetOutPath $_SCRIPTS_DIR

   File setup\install_db.bat
   File setup\install_java.bat
   File setup\install_node.bat
   File setup\uninstall.bat
   File setup\uninstall_tools.bat
   File setup\set_execution_policy.bat
   File /r *.cmd
   File /r *.ps1

   CreateDirectory $_LOGS_DIR

SectionEnd

Section "Mongodb (required)"

   ;SectionIn RO
   SetOutPath $TEMP

   nsExec::ExecToLog 'powershell -inputformat none -ExecutionPolicy Bypass -command Start-Process msiexec -ArgumentList \
   $\'/qn /norestart /l*v $_LOGS_DIR\db_install.log /i $_MONGO_INSTALL_KIT INSTALLDIR="$_MONGO_SERVER_PATH" ADDLOCAL="ServerNoService,ImportExportTools" SHOULD_INSTALL_COMPASS="0" $\' \
   -NoNewWindow -Wait'
   Pop $0

   ${If} $0 == 0
      DetailPrint "Database installed successfully. Setting environment variables"

      DetailPrint "Set MONGO_HOME environment variable"
      ${EnvVarUpdate} $0 "MONGO_HOME" "A" "HKLM" "$_MONGO_SERVER_PATH"

      DetailPrint "Add mongo bin to PATH environment variable"
      ${EnvVarUpdate} $0 "PATH" "A" "HKLM" "$_MONGO_SERVER_PATH\bin"

      CreateDirectory $_MONGO_DATA_PATH
   ${Else}
      DetailPrint "Database install method returned $0"
      MessageBox MB_OK "Installation failed. Please check the logs."
      Abort "Database server failed to install."
   ${EndIf}

SectionEnd

Section "Auth (required)"

   ;SectionIn RO

   SetOutPath $_AUTH_INSTALL_PATH

   ;TODO: use env var instead of hardcoding
   File /r auth\keycloak-8.0.0\*.*

   ;TODO : use if we have the ability to supply variables externally
   ;InitPluginsDir
   ;nsisunz::UnzipToStack "$_AUTH_INSTALL_OPTION-$_AUTH_VERSION.zip" "$TEMP\"
   ;nsisunz::UnzipToStack "$TEMP\keycloak-8.0.0.zip" "$TEMP\"

   ;Pop $0

   ;StrCmp $0 "success" ok
   ;   DetailPrint "Auth install method returned $0"
   ;   MessageBox MB_OK "Installation failed. Please check the logs."
   ;   Abort "Auth server failed to install."
   ;ok:

   ;nsExec::ExecToLog 'powershell -inputformat none -ExecutionPolicy Bypass -command set NOPAUSE=yes; & $_AUTH_INSTALL_PATH\bin\add-user-keycloak.bat --user admin --password 1337Hex'
   ;Pop $0

   ;${If} $0 == 0
      DetailPrint "Auth server installed successfully. Setting environment variables"

      DetailPrint "Set KEYCLOAK_HOME environment variable"
      ${EnvVarUpdate} $0 "KEYCLOAK_HOME" "A" "HKLM" "$_AUTH_INSTALL_PATH"

      DetailPrint "Add keycloak bin to PATH environment variable"
      ${EnvVarUpdate} $0 "PATH" "A" "HKLM" "$_AUTH_INSTALL_PATH\bin"
   ;${Else}
   ;   DetailPrint "Auth install method returned $0"
   ;   MessageBox MB_OK "Installation failed. Please check the logs."
   ;   Abort "Auth server failed to install."
   ;${EndIf}

SectionEnd

Section "Java (required)"

   ;SectionIn RO
   SetOutPath $TEMP

   nsExec::ExecToLog 'powershell -inputformat none -ExecutionPolicy Bypass -command Start-Process msiexec -ArgumentList \
   $\'/qn /norestart /l*v $_LOGS_DIR\java_install.log /i $_JAVA_INSTALL_KIT INSTALLDIR="$_JAVA_INSTALL_PATH"$\' -NoNewWindow -Wait'
   Pop $0

   ${If} $0 == 0
     DetailPrint "Java installed successfully. Setting environment variables"

     DetailPrint "Set JAVA_HOME environment variable"
     ${EnvVarUpdate} $0 "JAVA_HOME" "A" "HKLM" "$_JAVA_INSTALL_PATH"

     DetailPrint "Add java bin to PATH environment variable"
     ${EnvVarUpdate} $0 "PATH" "A" "HKLM" "$_JAVA_INSTALL_PATH\bin"
   ${Else}
      DetailPrint "Java install method returned $0"
      MessageBox MB_OK "Installation failed. Please check the logs."
      Abort "Java runtime environment failed to install."
   ${EndIf}

SectionEnd

Section "NodeJs (required)"

   ;SectionIn RO
   SetOutPath $TEMP

   nsExec::ExecToLog 'powershell -inputformat none -ExecutionPolicy Bypass -command Start-Process msiexec -ArgumentList \
   $\'/l*v $_LOGS_DIR\node_install.log /qn /i $_NODE_INSTALL_KIT INSTALLDIR=$_NODE_INSTALL_PATH $\' \
   -NoNewWindow -Wait'
   Pop $0

   ${If} $0 == 0
     DetailPrint "NodeJs installed successfully. Setting environment variables"

    DetailPrint "Set NODE_HOME environment variable"
    ${EnvVarUpdate} $0 "NODE_HOME" "A" "HKLM" "$_NODE_INSTALL_PATH"

    DetailPrint "Add nodejs bin to PATH environment variable"
    ${EnvVarUpdate} $0 "PATH" "A" "HKLM" "$_NODE_INSTALL_PATH"
   ${Else}
      DetailPrint "NodeJs install method returned $0"
      MessageBox MB_OK "Installation failed. Please check the logs."
      Abort "NodeJs failed to install."
   ${EndIf}

SectionEnd

Section "!EasyManage (required)"

   ;SectionIn RO

   ; extract run script files
   SetOutPath $_SCRIPTS_DIR

   File run\start.bat
   File run\stop.bat
   File run\status.bat
   File /oname=run-backend.bat run\deploy.bat
   File /oname=run-auth.bat run\run_auth.bat
   File /oname=import.bat run\fillDb.bat

   ; copy import data
   SetOutPath $_IMPORT_DIR
   File fillDb\*.json
   ; File auth\realm-export.json

   ; copy backend binary
   SetOutPath $_BACKEND_DIR
   File /oname=easy-manage.jar bin\resource-management.jar

   ; copy frontend structure
   SetOutPath $_FRONTEND_DIR
   File /r dist\*.*

   ; extract binary and configuration files
   SetOutPath $INSTDIR

   File /oname=README.md run\run_instructions.md
   SetFileAttributes README.md READONLY

   ; copy and customize backend configuration
   File /oname=application.properties run\production.application.properties

   ; copy and customize backend configuration
   File /oname=log4j2.properties run\production.log4j2.properties

   Var /Global BackendLogFileAbsolutePath
   ${StrRep} '$BackendLogFileAbsolutePath' '$_LOGS_DIR\$_BACKEND_LOG_FILE' '\' '\\'

   Var /Global BackendResourceStatusFileAbsolutePath
   ${StrRep} '$BackendResourceStatusFileAbsolutePath' '$_LOGS_DIR\$_BACKEND_RESOURCE_STATUS_FILE' '\' '\\'


   ${ConfigWrite} "application.properties" "server.port=" "$_BACKEND_PORT" $R0
   ${ConfigWrite} "application.properties" "logging.level.root=" "$_BACKEND_LOG_LEVEL" $R0
   ${ConfigWrite} "application.properties" "logging.file=" "$BackendLogFileAbsolutePath" $R0
   ${ConfigWrite} "application.properties" "resource.status.file=" "$BackendResourceStatusFileAbsolutePath" $R0

   SetOutPath $_BIN_DIR

   File /oname=easymanage.bat run\easymanage.bat
   SetFileAttributes easymanage.bat READONLY

   ; write config file
   DetailPrint "Writing config file in user profile folder ($PROFILE)"

   FileOpen $0 "$PROFILE\easymanage.conf" w
   FileClose $0

   ${ConfigWrite} "$PROFILE\easymanage.conf" "INSTALL_PATH=" "$INSTDIR" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "INSTALLED_VERSION=" "$_INSTALL_VERSION" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "AUTH_HOME=" "$_AUTH_INSTALL_PATH" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "AUTH_SERVER_PORT=" "$_AUTH_SERVER_PORT" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "MONGO_HOME=" "$_MONGO_SERVER_PATH" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "MONGO_DATA_PATH=" "$_MONGO_DATA_PATH" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "MONGO_LOG_PATH=" "$_MONGO_LOG_PATH" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "JAVA_HOME=" "$_JAVA_INSTALL_PATH" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "NODE_HOME=" "$_NODE_INSTALL_PATH" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "SERVER_ADDRESS=" "$_SERVER_ADDRESS" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "SERVER_PORT=" "$_SERVER_PORT" $R0
   ${ConfigWrite} "$PROFILE\easymanage.conf" "HTTP_SERVER_VERSION=" "$_HTTP_SERVER_VERSION" $R0

   ; set environment variables
   DetailPrint "Set EASYMAN_HOME environment variable"
   ${EnvVarUpdate} $0 "EASYMAN_HOME" "A" "HKLM" "$INSTDIR"

   DetailPrint "Add easymanage to PATH environment variable"
   ${EnvVarUpdate} $0 "PATH" "A" "HKLM" "$_BIN_DIR"

SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"

   CreateDirectory "$SMPROGRAMS\EasyManage"
   CreateShortCut "$SMPROGRAMS\EasyManage\Uninstall EasyManage.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0

   CreateShortCut "$SMPROGRAMS\EasyManage\EasyManage Start.lnk" "$_BIN_DIR\easymanage.bat" \
        "start" "$_BIN_DIR\easymanage.bat" 0 SW_SHOWNORMAL \
        "" "Start EasyManage"

   CreateShortCut "$SMPROGRAMS\EasyManage\EasyManage Stop.lnk" "$_BIN_DIR\easymanage.bat" \
           "stop" "$_BIN_DIR\easymanage.bat" 0 SW_SHOWNORMAL \
           "" "Stop EasyManage"

SectionEnd

Section /o "Desktop Shortcut" SectionX
    SetShellVarContext current

    SetOutPath $DESKTOP
    CreateShortCut "Start EasyManage.lnk" "$INSTDIR\bin\easymanage.bat" \
        "start" "$_BIN_DIR\easymanage.bat" 0 SW_SHOWNORMAL \
        "" "Start EasyManage"

    CreateShortCut "Stop EasyManage.lnk" "$INSTDIR\bin\easymanage.bat" \
        "stop" "$_BIN_DIR\easymanage.bat" 0 SW_SHOWNORMAL \
        "" "Stop EasyManage"
SectionEnd

; contents should be moved to a separate function
Section "-Cleanup"

    ; Remove files and uninstaller
    Delete $_SCRIPTS_DIR\install_db.bat
    Delete $_SCRIPTS_DIR\install_java.bat
    Delete $_SCRIPTS_DIR\install_node.bat
    Delete $INSTDIR\setup.bat

SectionEnd

;--------------------------------

# Uninstaller

Section "Uninstall"

   ; Read variables configured at install time

   ReadINIStr $_BIN_DIR "$PROFILE\\easymanage.ini" "paths" "BIN_PATH"
   ReadINIStr $_SCRIPTS_DIR "$PROFILE\\easymanage.ini" "paths" "SCRIPTS_PATH"
   ReadINIStr $_IMPORT_DIR "$PROFILE\\easymanage.ini" "paths" "IMPORT_PATH"
   ReadINIStr $_LOGS_DIR "$PROFILE\\easymanage.ini" "paths" "LOGS_PATH"
   ReadINIStr $_BACKEND_DIR "$PROFILE\\easymanage.ini" "paths" "BACKEND_PATH"
   ReadINIStr $_FRONTEND_DIR "$PROFILE\\easymanage.ini" "paths" "FRONTEND_PATH"

   ; Stop the application

   ExecWait '"$_BIN_DIR\easymanage.bat" stop' $0
   DetailPrint "EasyManage stop script returned $0"

   ; Unset environment variables

   DetailPrint "Unset EASYMAN_HOME environment variable"
   ${un.EnvVarUpdate} $0 "EASYMAN_HOME" "R" "HKLM" "$INSTDIR"

   DetailPrint "Remove easymanage bin dir from PATH environment variable"
   ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" "$_BIN_DIR"

   SetRegView 64

   ; Remove registry keys

   DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\EasyManage"
   DeleteRegKey HKLM SOFTWARE\NSIS_EasyManage

   ; Remove shortcuts, if any

   RMDir /r "$SMPROGRAMS\EasyManage"
   Delete "$DESKTOP\*EasyManage.lnk"

   ; Remove files

   RMDir /r $_SCRIPTS_DIR
   RMDir /r $_IMPORT_DIR
   RMDir /r $_BACKEND_DIR
   RMDir /r $_FRONTEND_DIR
   RMDir /r $_BIN_DIR

   ;Delete "$_LOGS_DIR\*.*"
   ;Delete $_LOGS_DIR

   ;Delete $_BIN_DIR\easymanage.bat

   ; uninstall Java

   ReadINIStr $_JAVA_INSTALL_OPTION "$PROFILE\\easymanage.ini" "dependencies" "JAVA_INSTALL_OPTION"
   ReadINIStr $_JAVA_INSTALL_PATH "$PROFILE\\easymanage.ini" "paths" "JAVA_INSTALL_PATH"
   ;nsExec::ExecToLog 'TODO'
   Pop $0

   DetailPrint "Uninstall java returned $0"

   DetailPrint "Unset JAVA_HOME environment variable"
   ${un.EnvVarUpdate} $0 "JAVA_HOME" "R" "HKLM" "$_JAVA_INSTALL_PATH"

   DetailPrint "Remove java bin dir from PATH environment variable"
   ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" "$_JAVA_INSTALL_PATH\bin"

   ; uninstall Db

   ReadINIStr $_DB_INSTALL_OPTION "$PROFILE\\easymanage.ini" "dependencies" "DB_INSTALL_OPTION"
   ReadINIStr $_MONGO_SERVER_PATH "$PROFILE\\easymanage.ini" "paths" "MONGO_SERVER_PATH"
   ReadINIStr $_MONGO_DATA_PATH "$PROFILE\\easymanage.ini" "paths" "MONGO_DATA_PATH"
   ReadINIStr $_MONGO_LOG_PATH "$PROFILE\\easymanage.ini" "paths" "MONGO_LOG_PATH"

   ;nsExec::ExecToLog 'TODO'
   Pop $0
   DetailPrint "Uninstall db returned $0"

   DetailPrint "Unset MONGO_HOME environment variable"
   ${un.EnvVarUpdate} $0 "MONGO_HOME" "R" "HKLM" "$_MONGO_SERVER_PATH"

   DetailPrint "Remove mongo bin dir from PATH environment variable"
   ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" "$_MONGO_SERVER_PATH\bin"

   ; Uninstall NodeJs

   ReadINIStr $_NODE_INSTALL_OPTION "$PROFILE\\easymanage.ini" "dependencies" "NODE_INSTALL_OPTION"
   ReadINIStr $_NODE_INSTALL_PATH "$PROFILE\\easymanage.ini" "paths" "NODE_INSTALL_PATH"

   ;nsExec::ExecToLog 'TODO'
   Pop $0
   DetailPrint "Uninstall node returned $0"

   DetailPrint "Unset NODE_HOME environment variable"
   ${un.EnvVarUpdate} $0 "NODE_HOME" "R" "HKLM" "$_NODE_INSTALL_PATH"

   DetailPrint "Remove node bin dir from PATH environment variable"
   ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" "$_NODE_INSTALL_PATH"

   ; Uninstall Auth

   ReadINIStr $_AUTH_INSTALL_OPTION "$PROFILE\\easymanage.ini" "dependencies" "AUTH_INSTALL_OPTION"
   ReadINIStr $_AUTH_INSTALL_PATH "$PROFILE\\easymanage.ini" "paths" "AUTH_INSTALL_PATH"

   RMDir /r "$_AUTH_INSTALL_PATH"

   DetailPrint "Unset AUTH_HOME environment variable"
   ${un.EnvVarUpdate} $0 "AUTH_HOME" "R" "HKLM" "$_AUTH_INSTALL_PATH"

   DetailPrint "Remove auth bin dir from PATH environment variable"
   ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" "$_AUTH_INSTALL_PATH\bin"

   ; Remove install folder
   Delete $INSTDIR\*.md
   Delete $INSTDIR\*.properties

   ; Remove metadata
   Delete $PROFILE\easymanage.conf
   Delete $PROFILE\easymanage.ini

   Delete $INSTDIR\uninstall.exe
   ;RMDir "$INSTDIR"

SectionEnd

;--------------------------------

# Output file

Section "-Output_log"

StrCpy $0 "$_LOGS_DIR\install.log"
Push $0
Call DumpLog

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
    StrCpy $0 "$INSTDIR\install_error.log"
    Push $0
    Call DumpLog
FunctionEnd

;--------------------------------

; Logging section

Function DumpLog
  Exch $5
  Push $0
  Push $1
  Push $2
  Push $3
  Push $4
  Push $6

  FindWindow $0 "#32770" "" $HWNDPARENT
  GetDlgItem $0 $0 1016
  StrCmp $0 0 exit
  FileOpen $5 $5 "w"
  StrCmp $5 "" exit
    SendMessage $0 ${LVM_GETITEMCOUNT} 0 0 $6
    System::Call '*(&t${NSIS_MAX_STRLEN})p.r3'
    StrCpy $2 0
    System::Call "*(i, i, i, i, i, p, i, i, i) i  (0, 0, 0, 0, 0, r3, ${NSIS_MAX_STRLEN}) .r1"
    loop: StrCmp $2 $6 done
      System::Call "User32::SendMessage(i, i, i, i) i ($0, ${LVM_GETITEMTEXT}, $2, r1)"
      System::Call "*$3(&t${NSIS_MAX_STRLEN} .r4)"
      FileWrite $5 "$4$\r$\n" ; Unicode will be translated to ANSI!
      IntOp $2 $2 + 1
      Goto loop
    done:
      FileClose $5
      System::Free $1
      System::Free $3
  exit:
    Pop $6
    Pop $4
    Pop $3
    Pop $2
    Pop $1
    Pop $0
    Exch $5
FunctionEnd
