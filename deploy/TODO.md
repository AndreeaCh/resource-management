## Resource Management Application 

### TODO

- nsis installer
    -> configure installer file logging, no need for intermediate log files
    -> make nsis script configurable, remove hardcoding
    -> language settings 
    -> modern look and feel
    -> configurable path for resource status history -> add file dialog
    -> consistency checks, verify things installed properly
    -> remove obsolete script files
    -> stop setting obsolete environment variables
    
- docker-on-linux install script
    -> proper docker image names
    -> simplify paths
    -> verbosity
    -> use release version instead of latest
    -> merge setup.sh and install.sh
    -> show help when invalid variables
- verbosity option for install scripts
- configurable backend port in application.properties
- installers to be copied at predefined location at maven deploy phase
- support for building distributables (including docker) under windows
- organize dist building with the use of build profiles
- precisely identify processes to be stopped(used port?)
- resolve path overwriting issue
- at uninstall cleanup PATH env var
- fix uninstall error (path to script not found)
- uninstall does not remove node and mongod for some reason
- proper log levels for production use
- change log pattern format to be more readable
- reduce log verbosity
- application log is missing .log extension after switching to log4j


### TO BE TESTED


### DONE

- nsis installer
    -> as an intermediate step, call install scripts directly instead of using setup.bat
    -> migrate batch scripts to nsis instructions if possible
    -> uninstall to remove installed files but keep logs and data

- maven convergence plugin
- move helper scripts under .scripts directory, keep main in the root directory
- slf4j binding - exclude logback dependency
- remove any env paths at uninstall (EASYMAN_HOME)
- configurable server url in env.js
- finalize uninstall script - extract uninstalling tools in a separate script (and run as administrator)
- make sure chocolatey and npm commands are available to the commands depending on them
- remove install path hardcoding  
- define application env var to be used in scripts
- rename bin and dist archives to reflect the target platform
- rename scripts directory to 'deploy' and create 'native' and 'docker' subfolders
- use client bin dist for windows native installer
- modify fillDb.bat and deploy.bat to take different arguments
 
 
# ISSUES
- nsis installer
    -> setup.bat file not playing well with install path containing spaces

### Installation

``` bash
setup.bat <install_directory> <path_to_archive>
```

### Release notes