## Resource Management Application 

### TODO

- rename bin and dist archives to reflect the target platform
- support for building distributable under windows
- installers to be copied predefined location at maven deploy phase
- organize dist building with the use of build profiles

### DONE

- rename scripts directory to 'deploy' and create 'native' and 'docker' subfolders
- use client bin dist for windows native installer
- modify fillDb.bat and deploy.bat to take different arguments
 

### Installation

``` bash
setup.bat <install_directory> <path_to_archive>
```

### Release notes