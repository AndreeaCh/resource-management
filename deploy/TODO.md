## Resource Management Application 

### TODO

- configurable server url in env.js
- configurable backend port in application.properties
- finalize uninstall script
- remove install path hardcoding  
- installers to be copied predefined location at maven deploy phase
- support for building distributables (including docker) under windows
- organize dist building with the use of build profiles

### DONE

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