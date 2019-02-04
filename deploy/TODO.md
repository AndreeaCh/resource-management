## Resource Management Application 

### TODO

- rename scripts directory to 'deploy' and create 'native' and 'docker' subfolders
- organize dist building with the use of build profiles
- support for building dist under windows
- native install to support demo mode instead at runtime
- installers to be copied predefined location at maven deploy phase  
- run built containers at maven deploy phase

### DONE

- use client bin dist for windows native installer
- modify fillDb.bat and deploy.bat to take different arguments
 

### Installation

``` bash
setup.bat <install_directory> <path_to_archive>
```

### Release notes