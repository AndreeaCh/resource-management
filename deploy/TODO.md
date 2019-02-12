## Resource Management Application 

### TODO

- configurable server url in env.js
- configurable backend port in application.properties
- installers to be copied predefined location at maven deploy phase
- support for building distributables (including docker) under windows
- organize dist building with the use of build profiles
- precisely identify processes to be stopped
- remove any env path at uninstall

### DONE

- finalize uninstall script - extract uninstalling tools in a separate script (and run as administrator)
- make sure chocolatey and npm commands are available to the commands depending on them
- remove install path hardcoding  
- define application env var to be used in scripts
- rename bin and dist archives to reflect the target platform
- rename scripts directory to 'deploy' and create 'native' and 'docker' subfolders
- use client bin dist for windows native installer
- modify fillDb.bat and deploy.bat to take different arguments
 

### Installation

``` bash
setup.bat <install_directory> <path_to_archive>
```

### Release notes